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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.UiUserConsentBinding
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentStateResult
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst
import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload
import com.tokopedia.usercomponents.userconsent.di.UserConsentComponentBuilder
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionParam
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

                    userConsentActionClickListener?.onChecklistClicked(isChecked)
                    singleCheckBoxChecked()
                }
            }

            multipleConsent.apply {
                checkboxPurposeA.setOnCheckedChangeListener { buttonView, isChecked ->
                    collection?.purposes?.let {
                        userConsentAnalytics.trackOnPurposeCheck(isChecked, it)
                    }

                    userConsentActionClickListener?.onChecklistClicked(isChecked)
                    multipleCheckBoxChecked()
                }

                checkboxPurposeB.setOnCheckedChangeListener { buttonView, isChecked ->
                    collection?.purposes?.let {
                        userConsentAnalytics.trackOnPurposeCheck(isChecked, it)
                    }

                    userConsentActionClickListener?.onChecklistClicked(isChecked)
                    multipleCheckBoxChecked()
                }

                checkboxPurposeC.setOnCheckedChangeListener { buttonView, isChecked ->
                    collection?.purposes?.let {
                        userConsentAnalytics.trackOnPurposeCheck(isChecked, it)
                    }

                    userConsentActionClickListener?.onChecklistClicked(isChecked)
                    multipleCheckBoxChecked()
                }
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
        return UserConsentPayload(collection?.id.orEmpty(), purposes)
    }

    private fun renderView() {
        var purposeText = ""
        collection?.purposes?.let {
            userConsentAnalytics.trackOnConsentView(it)
        }

        when {
            collection?.attributes?.collectionPointPurposeRequirement == UserConsentConst.MANDATORY -> {
                purposeText = ""
                collection?.purposes?.forEach {
                    purposeText += "${it.description} ${if (collection?.purposes?.size.orZero() > 1) ", " else ""}"
                }

                viewBinding?.buttonAction?.isEnabled = true
                viewBinding?.singleConsent?.apply {
                    if (collection?.attributes?.collectionPointStatementOnlyFlag == UserConsentConst.NO_CHECKLIST) {
                        checkboxPurposes.hide()
                        iconMandatoryInfo.show()
                    } else if (collection?.attributes?.collectionPointStatementOnlyFlag == UserConsentConst.CHECKLIST) {
                        checkboxPurposes.show()
                        iconMandatoryInfo.hide()
                    }

                    if (collection?.attributes?.policyNoticeType == UserConsentConst.TERM_CONDITION) {
                        descriptionPurposes.text = userConsentDescription?.generateTermConditionSinglePurposeText(
                            true,
                            collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                            purposeText
                        )
                    } else if (collection?.attributes?.policyNoticeType == UserConsentConst.TERM_CONDITION_POLICY) {
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

            collection?.attributes?.collectionPointPurposeRequirement == UserConsentConst.OPTIONAL &&
            collection?.attributes?.collectionPointStatementOnlyFlag == UserConsentConst.CHECKLIST -> {
                if (collection?.purposes?.size.orZero() > 1) {
                    viewBinding?.multipleConsent?.apply {
                        if (collection?.attributes?.policyNoticeType == UserConsentConst.TERM_CONDITION) {
                            textMainDescription.text = userConsentDescription?.generateTermConditionMultipleOptionalPurposeText(
                                collection?.attributes?.policyNoticeTnCPageID.orEmpty()
                            )
                        } else if (collection?.attributes?.policyNoticeType == UserConsentConst.TERM_CONDITION_POLICY) {
                            textMainDescription.text = userConsentDescription?.generateTermConditionPolicyMultipleOptionalPurposeText(
                                collection?.attributes?.policyNoticeTnCPageID.orEmpty(),
                                collection?.attributes?.policyNoticePolicyPageID.orEmpty()
                            )
                        }

                        textMainDescription.movementMethod = LinkMovementMethod.getInstance()

                        if (collection?.purposes?.size.orZero() == 1) {
                            checkboxPurposeA.text = collection?.purposes?.get(0)?.description
                            checkboxPurposeB.hide()
                            checkboxPurposeC.hide()
                        } else if (collection?.purposes?.size.orZero() == 2) {
                            checkboxPurposeA.text = collection?.purposes?.get(0)?.description
                            checkboxPurposeB.text = collection?.purposes?.get(1)?.description
                            checkboxPurposeC.hide()
                        } else if (collection?.purposes?.size.orZero() == 3) {
                            checkboxPurposeA.text = collection?.purposes?.get(0)?.description
                            checkboxPurposeB.text = collection?.purposes?.get(1)?.description
                            checkboxPurposeC.text = collection?.purposes?.get(2)?.description
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

    private fun multipleCheckBoxChecked() {
        viewBinding?.apply {
            buttonAction.isEnabled = when {
                collection?.purposes?.size.orZero() == 1 -> {
                    multipleConsent.checkboxPurposeA.isChecked
                }
                collection?.purposes?.size.orZero() == 2 -> {
                    multipleConsent.checkboxPurposeA.isChecked &&
                    multipleConsent.checkboxPurposeB.isChecked
                }
                collection?.purposes?.size.orZero() == 3 -> {
                    multipleConsent.checkboxPurposeA.isChecked &&
                    multipleConsent.checkboxPurposeB.isChecked &&
                    multipleConsent.checkboxPurposeC.isChecked
                }
                else -> false
            }
        }
    }

    override fun invalidate() {
        super.invalidate()

        viewBinding?.singleConsent?.apply {
            checkboxPurposes.isChecked = false
        }

        viewBinding?.multipleConsent?.apply {
            checkboxPurposeA.isChecked = false
            checkboxPurposeB.isChecked = false
            checkboxPurposeC.isChecked = false
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