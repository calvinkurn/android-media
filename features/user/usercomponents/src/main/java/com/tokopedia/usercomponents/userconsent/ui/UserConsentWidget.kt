package com.tokopedia.usercomponents.userconsent.ui

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.UiUserConsentBinding
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.CHECKLIST
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.NO_CHECKLIST
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.TERM_CONDITION
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.TERM_CONDITION_POLICY
import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload
import com.tokopedia.usercomponents.userconsent.common.UserConsentPurposeUiModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentStateResult
import com.tokopedia.usercomponents.userconsent.di.DaggerUserConsentComponent
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeAdapter
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeViewHolder
import javax.inject.Inject

class UserConsentWidget : FrameLayout, UserConsentPurposeViewHolder.UserConsentPurposeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: UserConsentViewModel? = null

    @Inject
    lateinit var userConsentAnalytics: UserConsentAnalytics

    private val viewBinding: UiUserConsentBinding? =
        UiUserConsentBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    private var lifecycleOwner: LifecycleOwner? = null
    private var consentCollectionParam: ConsentCollectionParam? = null
    private var userConsentActionClickListener: UserConsentActionClickListener? = null
    private var collection: UserConsentCollectionDataModel.CollectionPointDataModel? = null

    private var userConsentDescription: UserConsentDescription? = null
    private var userConsentPurposeAdapter: UserConsentPurposeAdapter? = null

    /** Set action button text */
    var actionText: String
        get() = viewBinding?.buttonAction?.text.toString()
        set(value) {
            viewBinding?.buttonAction?.text = value
        }

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        setupView(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        setupView(attributeSet)
    }

    private fun setupView(attributeSet: AttributeSet? = null) {
        initInjector()

        if (attributeSet != null) setAttributes(attributeSet)

        viewBinding?.apply {
            buttonAction.setOnClickListener {
                collection?.purposes?.let {
                    userConsentAnalytics.trackOnActionButtonClicked(it)
                }

                userConsentActionClickListener?.onActionClicked(generatePayloadData())
            }

            singleConsent.apply {
                checkboxPurposes.setOnCheckedChangeListener { buttonView, isChecked ->
                    collection?.purposes?.let {
                        userConsentAnalytics.trackOnPurposeCheck(isChecked, it)
                    }

                    userConsentActionClickListener?.onCheckedChange(isChecked)
                    singleCheckBoxChecked()
                }
            }

            multipleConsent.apply {
                userConsentPurposeAdapter = UserConsentPurposeAdapter(this@UserConsentWidget)
                recyclerPurposes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun initInjector() {
        context?.let {
            DaggerUserConsentComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun setAttributes(attributeSet: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.UserConsentWidget,
            NUMBER_ZERO,
            NUMBER_ZERO
        )

        actionText = typedArray.getString(R.styleable.UserConsentWidget_actionText)
            ?: context.getString(R.string.user_consent_default_action_text)

        typedArray.recycle()
    }

    private fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner) {
        val viewModelProvider = ViewModelProvider(viewModelStoreOwner, viewModelFactory)
        viewModel = viewModelProvider.get(UserConsentViewModel::class.java)
    }

    private fun initObserver() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.observe(it) { result ->
                when(result) {
                    is UserConsentStateResult.Loading -> {
                        setLoader(true)
                    }
                    is UserConsentStateResult.Fail -> {
                        setLoader(false)
                        showError(result.error)
                    }
                    is UserConsentStateResult.Success -> {
                        setLoader(false)
                        result.data?.let { data ->
                            collection = data.collectionPoints[NUMBER_ZERO]
                            onSuccessGetConsentCollection()
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessGetConsentCollection() {
        collection?.let {
            userConsentAnalytics.trackOnConsentView(it.purposes)
            userConsentDescription = UserConsentDescription(context, it)
        }

        renderView()
    }

    private fun generatePayloadData(): UserConsentPayload {
        val purposes: MutableList<UserConsentPayload.PurposeDataModel> = mutableListOf()
        collection?.purposes?.forEach {
            purposes.add(UserConsentPayload.PurposeDataModel(
                it.id,
                it.version,
                collection?.consentType.orEmpty()
            ))
        }
        return UserConsentPayload(collection?.id.orEmpty(), collection?.version.orEmpty(), purposes)
    }

    private fun renderView() {
        when {
            collection?.attributes?.collectionPointPurposeRequirement == MANDATORY -> {
                renderSinglePurpose()
            }

            collection?.attributes?.collectionPointPurposeRequirement == OPTIONAL &&
            collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST -> {
                renderMultiplePurpose()
            }
        }
    }

    private fun renderSinglePurpose() {
        var purposeText = ""
        if (collection?.purposes?.size.orZero() == NUMBER_ONE) {
            purposeText = collection?.purposes?.get(NUMBER_ZERO)?.description.orEmpty()
        } else {
            collection?.purposes?.forEachIndexed { index, purposeDataModel ->
                purposeText += when(index) {
                    (collection?.purposes?.size.orZero() - NUMBER_ONE) -> {
                        " & ${purposeDataModel.description}"
                    }

                    (collection?.purposes?.size.orZero() - NUMBER_TWO) -> {
                        purposeDataModel.description
                    }
                    else -> {
                        "${purposeDataModel.description}, "
                    }
                }
            }
        }

        viewBinding?.singleConsent?.apply {
            if (collection?.attributes?.collectionPointStatementOnlyFlag == NO_CHECKLIST) {
                checkboxPurposes.hide()
                iconMandatoryInfo.show()
                viewBinding.buttonAction.isEnabled = true
            } else if (collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST) {
                checkboxPurposes.show()
                iconMandatoryInfo.hide()
                viewBinding.buttonAction.isEnabled = false
            }

            if (collection?.attributes?.policyNoticeType == TERM_CONDITION) {
                descriptionPurposes.text = userConsentDescription?.generateTermConditionSinglePurposeText(
                    collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST,
                    collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                    purposeText
                )
            } else if (collection?.attributes?.policyNoticeType == TERM_CONDITION_POLICY) {
                descriptionPurposes.text = userConsentDescription?.generateTermConditionPolicySinglePurposeText(
                    collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST,
                    collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                    collection?.attributes?.policyNoticePolicyPageID.orEmpty(),
                    purposeText
                )
            }

            descriptionPurposes.movementMethod = LinkMovementMethod.getInstance()
        }?.root?.show()
    }

    private fun renderMultiplePurpose() {
        if (collection?.purposes?.size.orZero() > NUMBER_ONE) {
            viewBinding?.multipleConsent?.apply {
                if (collection?.attributes?.policyNoticeType == TERM_CONDITION) {
                    textMainDescription.text = userConsentDescription?.generateTermConditionMultipleOptionalPurposeText(
                        collection?.attributes?.policyNoticeTnCPageID.orEmpty()
                    )
                } else if (collection?.attributes?.policyNoticeType == TERM_CONDITION_POLICY) {
                    textMainDescription.text = userConsentDescription?.generateTermConditionPolicyMultipleOptionalPurposeText(
                        collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                        collection?.attributes?.policyNoticePolicyPageID.orEmpty()
                    )
                }

                textMainDescription.movementMethod = LinkMovementMethod.getInstance()
                recyclerPurposes.adapter = userConsentPurposeAdapter
                userConsentPurposeAdapter?.clearAllItems()
                collection?.purposes?.forEach {
                    userConsentPurposeAdapter?.addItem(UserConsentPurposeUiModel(it))
                }
            }?.root?.show()
        }
    }

    private fun singleCheckBoxChecked() {
        viewBinding?.apply {
            buttonAction.isEnabled = singleConsent.checkboxPurposes.isChecked
        }
    }

    private fun setLoader(isLoading: Boolean) {
        viewBinding?.apply {
            if (isLoading) {
                consentLoader.root.show()

                singleConsent.root.hide()
                multipleConsent.root.hide()
                consentError.hide()
            } else {
                consentLoader.root.hide()
            }
        }
    }

    private fun showError(throwable: Throwable) {
        viewBinding?.consentError?.apply {
            title?.text = resources.getString(R.string.user_consent_failed_load_collection)
            refreshBtn?.setOnClickListener {
                consentCollectionParam?.let { param ->
                    viewModel?.getConsentCollection(param)
                }
            }
        }?.show()

        userConsentActionClickListener?.onFailed(throwable)
    }

    override fun onCheckedChange(
        isChecked: Boolean,
        purposeDataModel: UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel,
    ) {
        userConsentActionClickListener?.onCheckedChange(isChecked)
        userConsentAnalytics.trackOnPurposeCheckOnOptional(isChecked, purposeDataModel)

        if (purposeDataModel.attribute.alwaysMandatory == MANDATORY) {
            viewBinding?.buttonAction?.isEnabled = isChecked
        } else {
            val isAllChecked = userConsentPurposeAdapter?.listCheckBoxView?.all {
                it.isChecked
            }

            if (isAllChecked == true) {
                viewBinding?.buttonAction?.isEnabled = true
            }
        }
    }

    override fun invalidate() {
        super.invalidate()

        viewBinding?.singleConsent?.apply {
            checkboxPurposes.isChecked = false
        }

        viewBinding?.buttonAction?.isEnabled = false
        userConsentPurposeAdapter?.clearAllItems()
    }

    fun load(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        consentCollectionParam: ConsentCollectionParam,
        userConsentActionClickListener: UserConsentActionClickListener
    ) {
        this.lifecycleOwner = lifecycleOwner
        this.consentCollectionParam = consentCollectionParam
        this.userConsentActionClickListener = userConsentActionClickListener

        invalidate()
        initViewModel(viewModelStoreOwner)
        initObserver()
        viewModel?.getConsentCollection(consentCollectionParam)
    }

    fun onDestroy() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.removeObservers(it)
        }

        lifecycleOwner = null
    }

    companion object {
        private const val NUMBER_ZERO = 0
        private const val NUMBER_ONE = 1
        private const val NUMBER_TWO = 2
    }
}