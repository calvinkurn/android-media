package com.tokopedia.saldodetails.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlInfoListResponse
import com.tokopedia.saldodetails.response.model.GqlSpAnchorListResponse
import com.tokopedia.saldodetails.utils.Success
import com.tokopedia.saldodetails.view.activity.SaldoWebViewActivity
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.BUNDLE_PARAM_SELLER_DETAILS
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.BUNDLE_PARAM_SELLER_DETAILS_ID
import com.tokopedia.saldodetails.viewmodels.MerchantSaldoPriorityViewModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import javax.inject.Inject

class MerchantSaldoPriorityFragment : BaseDaggerFragment() {

    companion object {

        private val NONE = "none"
        private val DEFAULT = "default"
        private val WARNING = "warning"
        private val DANGER = "danger"

        fun newInstance(bundle: Bundle): Fragment {
            val merchantSaldoPriorityFragment = MerchantSaldoPriorityFragment()
            merchantSaldoPriorityFragment.arguments = bundle
            return merchantSaldoPriorityFragment
        }
    }

    private var spTitle: TextView? = null
    private var spNewTitle: Label? = null
    private var spDescription: TextView? = null
    private var spKYCStatusLayout: RelativeLayout? = null
    private var spKYCShortDesc: TextView? = null
    private var spKYCLongDesc: TextView? = null
    private var spDetailListLinearLayout: LinearLayout? = null
    private var spActionListLinearLayout: LinearLayout? = null
    private var spEnableSwitchCompat: SwitchUnify? = null
    private var spRightArrow: ImageView? = null
    private var spStatusInfoIcon: ImageView? = null
    private var interactionListener: InteractionListener? = null

    private var sellerDetails: GqlDetailsResponse? = null

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var merchantSaldoPriorityViewModel: MerchantSaldoPriorityViewModel

    private var originalSwitchState: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.saldodetails.R.layout.fragment_saldo_prioritas,
            container,
            false
        )
        if (savedInstanceState == null) {
            val bundle = arguments
            val saveInstanceCacheManagerId = bundle?.getString(BUNDLE_PARAM_SELLER_DETAILS_ID) ?: ""
            val saveInstanceCacheManager = context?.let {
                SaveInstanceCacheManager(it, saveInstanceCacheManagerId)
            }
            sellerDetails = saveInstanceCacheManager?.get(
                BUNDLE_PARAM_SELLER_DETAILS,
                GqlDetailsResponse::class.java
            )
            initViews(view)
            setViewModelObservers()
        }
        return view
    }

    private fun setViewModelObservers() {
        merchantSaldoPriorityViewModel.gqlUpdateSaldoStatusLiveData.observe(context as AppCompatActivity,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.merchantSaldoStatus?.isSuccess!!) {
                            onSaldoStatusUpdateSuccess(
                                it.data.merchantSaldoStatus?.value
                                    ?: false
                            )
                        } else {
                            onSaldoStatusUpdateError("")
                        }
                        hideProgressLoading()
                    }
                    else -> {
                        onSaldoStatusUpdateError("")
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            populateData()
            initListeners()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            this.interactionListener = context as InteractionListener?
        } catch (e: Exception) {

        }
    }

    private fun initViews(view: View) {
        spTitle = view.findViewById(com.tokopedia.saldodetails.R.id.sp_title)
        spNewTitle = view.findViewById(com.tokopedia.saldodetails.R.id.sp_new_title)
        spDescription = view.findViewById(com.tokopedia.saldodetails.R.id.sp_description)
        spKYCStatusLayout = view.findViewById(com.tokopedia.saldodetails.R.id.sp_kyc_status)
        spKYCShortDesc = view.findViewById(com.tokopedia.saldodetails.R.id.sp_kyc_short_desc)
        spKYCLongDesc = view.findViewById(com.tokopedia.saldodetails.R.id.sp_kyc_long_desc)
        spDetailListLinearLayout = view.findViewById(com.tokopedia.saldodetails.R.id.sp_detail_list)
        spActionListLinearLayout = view.findViewById(com.tokopedia.saldodetails.R.id.sp_action_list)
        spEnableSwitchCompat = view.findViewById(com.tokopedia.saldodetails.R.id.sp_enable_switch)
        spRightArrow = view.findViewById(com.tokopedia.saldodetails.R.id.sp_right_arrow)
        spStatusInfoIcon = view.findViewById(com.tokopedia.saldodetails.R.id.sp_status_info_icon)
    }

    private fun initListeners() {
        spEnableSwitchCompat!!.setOnCheckedChangeListener { _, isChecked ->
            if (originalSwitchState == isChecked) {
                return@setOnCheckedChangeListener
            }
            showDialog(isChecked)
        }

        if (sellerDetails!!.isBoxShowPopup) {
            spKYCStatusLayout!!.setOnClickListener {
                val userStatusInfoBottomSheet = UserStatusInfoBottomSheet()
                userStatusInfoBottomSheet.setBody(sellerDetails!!.popupDesc!!)
                userStatusInfoBottomSheet.setBottomSheetTitle(sellerDetails!!.popupTitle!!)
                userStatusInfoBottomSheet.setButtonText(sellerDetails!!.popupButtonText!!)
                userStatusInfoBottomSheet.show(childFragmentManager, UserStatusInfoBottomSheet.TAG)
            }
        }
    }

    private fun showDialog(isChecked: Boolean) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                if (isChecked) {
                    setTitle(it.getString(com.tokopedia.saldodetails.R.string.sp_enable_title))
                    setDescription(it.getString(com.tokopedia.saldodetails.R.string.sp_enable_desc))
                    setPrimaryCTAText(it.getString(com.tokopedia.saldodetails.R.string.sp_btn_ok_enable))
                } else {
                    setTitle(it.getString(com.tokopedia.saldodetails.R.string.sp_disable_title))
                    if (sellerDetails!!.status == 5) {
                        setDescription(it.getString(com.tokopedia.saldodetails.R.string.sp_disable_desc_long))
                    } else {
                        setDescription(Html.fromHtml(it.getString(com.tokopedia.saldodetails.R.string.sp_disable_desc)))
                    }

                    setPrimaryCTAText(it.getString(com.tokopedia.saldodetails.R.string.sp_btn_ok_disable))
                }
                setSecondaryCTAText(it.getString(com.tokopedia.saldodetails.R.string.sp_btn_cancel))
                setPrimaryCTAClickListener {
                    dismiss()
                    showProgressLoading()
                    merchantSaldoPriorityViewModel.updateSellerSaldoStatus(isChecked)
                }
                setSecondaryCTAClickListener {
                    dismiss()
                    spEnableSwitchCompat!!.isChecked = isChecked.not()
                }
                show()
            }
        }
    }

    private fun populateData() {
        populateSellerDetail()
        populateBoxDetail()
        populateAnchorList()
    }

    private fun populateSellerDetail() {
        if (sellerDetails!!.isShowToggle) {
            spEnableSwitchCompat!!.show()
            spEnableSwitchCompat!!.isChecked = sellerDetails!!.isEnabled
            spEnableSwitchCompat!!.isClickable = true
            originalSwitchState = sellerDetails!!.isEnabled
        } else {
            spEnableSwitchCompat!!.gone()
        }

        if (!TextUtils.isEmpty(sellerDetails!!.title)) {
            spTitle!!.text = sellerDetails!!.title
        } else {
            spTitle!!.text = "Saldo Prioritas"
        }

        if (sellerDetails!!.isShowNewLogo) {
            spNewTitle!!.show()
        } else {
            spNewTitle!!.gone()
        }

        if (!TextUtils.isEmpty(sellerDetails!!.description)) {
            spDescription!!.text = Html.fromHtml(sellerDetails!!.description)
            spDescription!!.show()
        } else {
            spDescription!!.gone()
        }
    }

    private fun populateBoxDetail() {
        if (!TextUtils.isEmpty(sellerDetails!!.boxTitle)) {
            spKYCStatusLayout!!.show()
            spKYCShortDesc!!.show()
            spKYCShortDesc!!.text = Html.fromHtml(sellerDetails!!.boxTitle)

            if (!TextUtils.isEmpty(sellerDetails!!.boxDesc)) {
                spKYCLongDesc!!.show()
                spKYCLongDesc!!.text = Html.fromHtml(sellerDetails!!.boxDesc)
            } else {
                spKYCLongDesc!!.gone()
            }

            if (sellerDetails!!.isShowRightArrow) {
                spRightArrow!!.show()
            } else {
                spRightArrow!!.gone()
            }
            setBoxBackground()
        } else {
            spKYCStatusLayout!!.gone()
        }
    }

    private fun populateAnchorList() {
        if (sellerDetails!!.infoList != null && sellerDetails!!.infoList!!.isNotEmpty()) {
            populateInfolistData(sellerDetails!!.infoList)
        }

        if (sellerDetails!!.anchorList != null && sellerDetails!!.anchorList!!.isNotEmpty()) {
            populateAnchorListData(sellerDetails!!.anchorList)
        }
    }

    private fun setBoxBackground() {
        context?.let { context ->
            val boxType = sellerDetails!!.boxType
            if (boxType!!.equals(NONE, ignoreCase = true)) {
                spStatusInfoIcon!!.gone()
            } else if (boxType.equals(DEFAULT, ignoreCase = true)) {
                val drawable = getIconUnifyDrawable(
                    context,
                    com.tokopedia.iconunify.R.drawable.iconunify_information,
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                )
                spStatusInfoIcon!!.setImageDrawable(drawable)
                spKYCStatusLayout!!.background =
                    resources.getDrawable(com.tokopedia.saldodetails.R.drawable.sp_bg_rounded_corners_green)
            } else if (boxType.equals(WARNING, ignoreCase = true)) {
                val drawable = getIconUnifyDrawable(
                    context,
                    com.tokopedia.iconunify.R.drawable.iconunify_information,
                    com.tokopedia.unifyprinciples.R.color.Unify_Y300
                )
                spStatusInfoIcon!!.setImageDrawable(drawable)
                spKYCStatusLayout!!.background =
                    resources.getDrawable(com.tokopedia.saldodetails.R.drawable.bg_rounded_corner_warning)
            } else if (boxType.equals(DANGER, ignoreCase = true)) {
                val drawable = getIconUnifyDrawable(
                    context,
                    com.tokopedia.iconunify.R.drawable.iconunify_information,
                    com.tokopedia.unifyprinciples.R.color.Unify_R200
                )
                spStatusInfoIcon!!.setImageDrawable(drawable)
                spKYCStatusLayout!!.background =
                    resources.getDrawable(com.tokopedia.saldodetails.R.drawable.bg_rounded_corner_danger)
            }
        }
    }

    private fun populateAnchorListData(anchorList: List<GqlSpAnchorListResponse?>?) {
        val layoutInflater = layoutInflater
        spActionListLinearLayout!!.removeAllViews()

        if (anchorList == null) {
            return
        }
        val list_size = anchorList.size
        for (i in list_size - 1 downTo 0) {

            val gqlAnchorListResponse = anchorList[i]
            if (gqlAnchorListResponse != null) {
                val view = layoutInflater.inflate(
                    com.tokopedia.saldodetails.R.layout.layout_anchor_list,
                    null
                )
                val anchorLabel =
                    view.findViewById<TextView>(com.tokopedia.saldodetails.R.id.anchor_label)

                anchorLabel.text = gqlAnchorListResponse.label
                anchorLabel.tag = gqlAnchorListResponse.url

                try {
                    anchorLabel.setTextColor(Color.parseColor(gqlAnchorListResponse.color))
                } catch (e: Exception) {
                    anchorLabel.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400))
                }

                anchorLabel.setOnClickListener { v ->
                    if (!TextUtils.isEmpty(gqlAnchorListResponse.url) && context != null) {
                        saldoDetailsAnalytics.eventAnchorLabelClick(anchorLabel.text.toString())
                        startActivity(
                            SaldoWebViewActivity.getWebViewIntent(
                                requireContext(),
                                gqlAnchorListResponse.url
                            )
                        )
                    }
                }
                spActionListLinearLayout!!.addView(view)
            }
        }
    }

    private fun populateInfolistData(infoList: List<GqlInfoListResponse>?) {
        val layoutInflater = layoutInflater
        spDetailListLinearLayout!!.removeAllViews()

        if (infoList == null) {
            return
        }
        for (infoList1 in infoList) {

            val view =
                layoutInflater.inflate(com.tokopedia.saldodetails.R.layout.layout_info_list, null)
            val infoLabel =
                view.findViewById<TextView>(com.tokopedia.saldodetails.R.id.info_label_text_view)
            val infoValue =
                view.findViewById<TextView>(com.tokopedia.saldodetails.R.id.info_value_text_view)

            infoLabel.text = infoList1.label
            infoValue.text = infoList1.value

            spDetailListLinearLayout!!.addView(view)
        }
    }

    override fun initInjector() {
        activity?.let {
            val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(it)
            saldoDetailsComponent.inject(this)
        }

        if (context is AppCompatActivity) {
            val viewModelProvider =
                ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            merchantSaldoPriorityViewModel =
                viewModelProvider[MerchantSaldoPriorityViewModel::class.java]
        }
    }


    override fun getScreenName(): String? {
        return null
    }

    private fun showProgressLoading() {
        if (interactionListener != null) {
            interactionListener!!.showLoading()
        }
    }

    private fun hideProgressLoading() {
        if (interactionListener != null) {
            interactionListener!!.dismissLoading()
        }
    }

    private fun onSaldoStatusUpdateError(errorMessage: String) {
        val check = spEnableSwitchCompat!!.isChecked
        spEnableSwitchCompat!!.isChecked = !check
        NetworkErrorHelper.showRedSnackbar(activity, errorMessage)
    }

    private fun onSaldoStatusUpdateSuccess(newState: Boolean) {
        originalSwitchState = newState
        NetworkErrorHelper.showGreenSnackbarShort(
            activity,
            resources.getString(com.tokopedia.saldodetails.R.string.saldo_status_updated_success)
        )
    }

    interface InteractionListener {
        fun showLoading()
        fun dismissLoading()
    }

}
