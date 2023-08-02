package com.tokopedia.payment.setting.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.add.view.activity.AddCreditCardActivity
import com.tokopedia.payment.setting.authenticate.view.activity.AuthenticateCreditCardActivity
import com.tokopedia.payment.setting.databinding.FragmentSettingListPaymentBinding
import com.tokopedia.payment.setting.detail.view.activity.DetailCreditCardActivity
import com.tokopedia.payment.setting.di.SettingPaymentComponent
import com.tokopedia.payment.setting.list.analytics.PaymentSettingListAnalytics
import com.tokopedia.payment.setting.list.model.PaymentSignature
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.model.SettingListCardCounterModel
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener
import com.tokopedia.payment.setting.list.view.viewmodel.SettingsListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SettingListPaymentFragment :
    BaseListFragment<SettingListPaymentModel, SettingListPaymentAdapterTypeFactory>(),
    SettingListActionListener {

    private var paymentSignature: PaymentSignature? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var analytics: PaymentSettingListAnalytics

    private var binding by autoClearedNullable<FragmentSettingListPaymentBinding>()

    private val settingsListViewModel: SettingsListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(SettingsListViewModel::class.java)
    }

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context) }

    override fun initInjector() {
        getComponent(SettingPaymentComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingListPaymentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        context?.let {
            val dividerItemDecoration = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(it, R.drawable.divider_list_card)?.let { it1 -> dividerItemDecoration.setDrawable(it1) }
            getRecyclerView(view)?.addItemDecoration(dividerItemDecoration)
        }
        binding?.authenticateCreditCard?.setOnClickListener {
            analytics.sendEventClickAuthenticate()
            activity?.run {
                showLoadingDialog()
                settingsListViewModel.checkVerificationPhone()
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        settingsListViewModel.phoneVerificationStatusLiveData.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    hideLoadingDialog()
                    onSuccessVerifPhone()
                } else {
                    hideLoadingDialog()
                    onNeedVerifPhone()
                }
            }
        )

        settingsListViewModel.bannerAndCardListResultLiveData.observe(viewLifecycleOwner) {
            val cardListResult = it.first
            val bannerResult = it.second

            if (cardListResult != null && bannerResult != null) {
                val list = mutableListOf<SettingListPaymentModel>()

                when (cardListResult) {
                    is Success -> {
                        onPaymentSignature(cardListResult.data.paymentSignature)
                        val cards = cardListResult.data.creditCard.cards
                        list.add(SettingListCardCounterModel(cards?.size ?: 0))
                        list.addAll(cards ?: arrayListOf())
                    }
                    is Fail -> showGetListError(cardListResult.throwable)
                }

                if (bannerResult is Success && bannerResult.data.assets.isNotEmpty()) {
                    list.add(0, bannerResult.data)
                }

                renderList(list)
            }
        }
    }

    override fun getAdapterTypeFactory(): SettingListPaymentAdapterTypeFactory {
        return SettingListPaymentAdapterTypeFactory(this)
    }

    override fun onItemClicked(data: SettingListPaymentModel?) {
        when (data) {
            is SettingBannerModel -> {
                RouteManager.route(this.context, data.buttonRedirectUrl)
                sendEventClickBanner(data)
            }
            is SettingListCardCounterModel -> {}
            else -> {
                analytics.sendEventClickCard()
                activity?.run {
                    this@SettingListPaymentFragment.startActivityForResult(DetailCreditCardActivity.createIntent(this, data), REQUEST_CODE_DETAIL_CREDIT_CARD)
                }
            }
        }
    }

    private fun sendEventClickBanner(data: SettingBannerModel) {
        when (data.code) {
            SettingBannerModel.CODE_REGISTER_COBRAND -> {
                analytics.sendEventClickBannerUserNotYetHaveCobrand()
            }
            SettingBannerModel.CODE_ON_PROGRESS -> {
                analytics.sendEventClickBannerUserCobrandOnProgress()
            }
            SettingBannerModel.CODE_ACTIVATION -> {
                analytics.sendEventClickBannerUserCobrandActivate()
            }
        }
    }

    private fun sendEventViewBanner(data: SettingBannerModel) {
        when (data.code) {
            SettingBannerModel.CODE_REGISTER_COBRAND -> {
                analytics.sendEventViewBannerUserNotYetHaveCobrand()
            }
            SettingBannerModel.CODE_ON_PROGRESS -> {
                analytics.sendEventViewBannerUserCobrandOnProgress()
            }
            SettingBannerModel.CODE_ACTIVATION -> {
                analytics.sendEventViewBannerUserCobrandActivate()
            }
            SettingBannerModel.CODE_SAVE_CARD -> {
                analytics.sendEventViewBannerUserCobrandAdd()
            }
        }
    }

    override fun onClickAddCard() {
        analytics.sendEventClickAddCard()
        activity?.run {
            paymentSignature?.let { paymentSignature ->
                this@SettingListPaymentFragment
                    .startActivityForResult(AddCreditCardActivity.createIntent(this, paymentSignature), REQUEST_CODE_ADD_CREDIT_CARD)
            }
        }
    }

    override fun onViewBanner(element: SettingBannerModel) {
        sendEventViewBanner(element)
    }

    override fun onPaymentListImpressed() {
        analytics.sendEventViewListCreditOrDebitCard()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DETAIL_CREDIT_CARD, REQUEST_CODE_ADD_CREDIT_CARD, REQUEST_CODE_AUTH_CREDIT_CARD -> loadInitialData()
                REQUEST_CODE_VERIF_PHONE -> onSuccessVerifPhone()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getScreenName() = ""

    override fun isLoadMoreEnabledByDefault() = false

    override fun showGetListError(throwable: Throwable?) {
        hideAuthPaymentView()
        super.showGetListError(throwable)
    }

    override fun renderList(list: MutableList<SettingListPaymentModel>) {
        adapter.clearAllElements()

        val cardSize = list.size - list.filter {
            it is SettingListCardCounterModel || it is SettingBannerModel
        }.size

        if (cardSize in CARD_LIST_RANGE_FOR_ADD_MORE_CARD) {
            list.add(SettingListAddCardModel())
        }
        super.renderList(list)
        showAuthPaymentView()
    }

    private fun showLoadingDialog() {
        progressDialog.show()
    }

    private fun hideLoadingDialog() {
        progressDialog.hide()
    }

    private fun onSuccessVerifPhone() {
        activity?.run {
            this@SettingListPaymentFragment.startActivityForResult(AuthenticateCreditCardActivity.createIntent(this), REQUEST_CODE_AUTH_CREDIT_CARD)
        }
    }

    private fun onNeedVerifPhone() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.payment_label_title_dialog_verif_phone))
            dialog.setDescription(getString(R.string.payment_label_desc_dialog_verif))
            dialog.setPrimaryCTAText(getString(R.string.payment_label_continue_dialog_verif))
            dialog.setSecondaryCTAText(getString(R.string.payment_label_cancel_dialog_verif))
            dialog.setPrimaryCTAClickListener {
                activity?.run {
                    val intent = RouteManager.getIntent(applicationContext, ApplinkConstInternalUserPlatform.SETTING_PROFILE)
                    this@SettingListPaymentFragment.startActivityForResult(intent, REQUEST_CODE_VERIF_PHONE)
                }
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun onPaymentSignature(paymentSignature: PaymentSignature?) {
        this.paymentSignature = paymentSignature
    }

    override fun loadData(page: Int) {
        settingsListViewModel.getCreditCardList()
        settingsListViewModel.getSettingBanner()
        hideAuthPaymentView()
    }

    private fun hideAuthPaymentView() {
        binding?.run {
            dividerListPayment.visibility = View.GONE
            authenticateCreditCard.visibility = View.GONE
        }
    }

    private fun showAuthPaymentView() {
        binding?.run {
            dividerListPayment?.visibility = View.VISIBLE
            authenticateCreditCard?.visibility = View.VISIBLE
        }
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    private fun removeObservers() {
        settingsListViewModel.phoneVerificationStatusLiveData.removeObservers(this)
        settingsListViewModel.bannerAndCardListResultLiveData.removeObservers(this)
    }

    companion object {
        private val CARD_LIST_RANGE_FOR_ADD_MORE_CARD = 0..3
        const val REQUEST_CODE_DETAIL_CREDIT_CARD = 4213
        const val REQUEST_CODE_ADD_CREDIT_CARD = 4273
        const val REQUEST_CODE_AUTH_CREDIT_CARD = 4275
        const val REQUEST_CODE_VERIF_PHONE = 3734

        fun createInstance(): SettingListPaymentFragment {
            return SettingListPaymentFragment()
        }
    }
}
