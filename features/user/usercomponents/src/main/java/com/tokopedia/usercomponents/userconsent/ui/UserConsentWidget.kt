package com.tokopedia.usercomponents.userconsent.ui

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.usercomponents.userconsent.di.UserConsentComponentBuilder
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeAdapter
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeViewHolder
import javax.inject.Inject

class UserConsentWidget : FrameLayout {

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
                userConsentPurposeAdapter = UserConsentPurposeAdapter(object : UserConsentPurposeViewHolder.UserConsentPurposeListener {
                    override fun onCheckedChange(
                        isChecked: Boolean,
                        purposeDataModel: UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel
                    ) {
                        userConsentActionClickListener?.onCheckedChange(isChecked)

                        val isMandatoryPurpose = collection?.purposes?.find {
                            it.id == purposeDataModel.id
                        }?.attribute?.alwaysMandatory == MANDATORY

                        if (isMandatoryPurpose) {
                            viewBinding.buttonAction.isEnabled = isChecked
                        } else {
                            val isAllChecked = userConsentPurposeAdapter?.listCheckBoxView?.all {
                                it.isChecked
                            }

                            viewBinding.buttonAction.isEnabled = isAllChecked == true
                        }
                    }
                })

                recyclerPurposes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun initInjector() {
        context?.let {
            UserConsentComponentBuilder
                .getComponent(it)
                .inject(this)

            if (it is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
                viewModel = viewModelProvider[UserConsentViewModel::class.java]
            }
        }
    }

    private fun setAttributes(attributeSet: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.UserConsentWidget,
            0,
            0
        )

        actionText = typedArray.getString(R.styleable.UserConsentWidget_actionText)
            ?: context.getString(R.string.user_consent_default_action_text)

        typedArray.recycle()
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
                            collection = data.collectionPoints[0]
                            onSuccessGetConsentCollection()
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessGetConsentCollection() {
        collection?.let {
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
        var purposeText = ""
        collection?.purposes?.let {
            userConsentAnalytics.trackOnConsentView(it)
        }

        when {
            collection?.attributes?.collectionPointPurposeRequirement == MANDATORY -> {
                purposeText = ""
                if (collection?.purposes?.size.orZero() == 1) {
                    purposeText = collection?.purposes?.get(0)?.description.orEmpty()
                } else {
                    collection?.purposes?.forEachIndexed { index, purposeDataModel ->
                        purposeText += when(index) {
                            (collection?.purposes?.size.orZero() - 1) -> {
                                " & ${purposeDataModel.description}"
                            }

                            (collection?.purposes?.size.orZero() - 2) -> {
                                purposeDataModel.description
                            }
                            else -> {
                                "${purposeDataModel.description}, "
                            }
                        }
                    }
                }

                viewBinding?.buttonAction?.isEnabled = true
                viewBinding?.singleConsent?.apply {
                    if (collection?.attributes?.collectionPointStatementOnlyFlag == NO_CHECKLIST) {
                        checkboxPurposes.hide()
                        iconMandatoryInfo.show()
                    } else if (collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST) {
                        checkboxPurposes.show()
                        iconMandatoryInfo.hide()
                    }

                    if (collection?.attributes?.policyNoticeType == TERM_CONDITION) {
                        descriptionPurposes.text = userConsentDescription?.generateTermConditionSinglePurposeText(
                            true,
                            collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                            purposeText
                        )
                    } else if (collection?.attributes?.policyNoticeType == TERM_CONDITION_POLICY) {
                        descriptionPurposes.text = userConsentDescription?.generateTermConditionPolicySinglePurposeText(
                            true,
                            collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                            collection?.attributes?.policyNoticePolicyPageID.orEmpty(),
                            purposeText
                        )
                    }

                    descriptionPurposes.movementMethod = LinkMovementMethod.getInstance()
                }?.root?.show()
            }

            collection?.attributes?.collectionPointPurposeRequirement == OPTIONAL &&
            collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST -> {
                if (collection?.purposes?.size.orZero() > 1) {
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
        }
    }

    private fun singleCheckBoxChecked() {
        viewBinding?.apply {
            buttonAction.isEnabled = singleConsent.checkboxPurposes.isChecked
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
            title?.text = resources.getString(com.tokopedia.usercomponents.R.string.user_consent_failed_load_collection)
            refreshBtn?.setOnClickListener {
                consentCollectionParam?.let { param ->
                    viewModel?.getConsentCollection(param)
                }
            }
        }?.show()

        userConsentActionClickListener?.onFailed(throwable)
    }

    fun load(
        lifecycleOwner: LifecycleOwner,
        consentCollectionParam: ConsentCollectionParam,
        userConsentActionClickListener: UserConsentActionClickListener
    ) {
        this.lifecycleOwner = lifecycleOwner
        this.consentCollectionParam = consentCollectionParam
        this.userConsentActionClickListener = userConsentActionClickListener

        initObserver()
        viewModel?.getConsentCollection(consentCollectionParam)

        invalidate()
    }

    fun onDetach() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.removeObservers(it)
        }
    }
}