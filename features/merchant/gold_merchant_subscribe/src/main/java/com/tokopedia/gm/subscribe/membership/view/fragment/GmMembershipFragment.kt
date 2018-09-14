package com.tokopedia.gm.subscribe.membership.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.core.util.DateFormatUtils
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.data.model.GetMembershipData
import com.tokopedia.gm.subscribe.membership.di.GmSubscribeMembershipComponent
import com.tokopedia.gm.subscribe.membership.view.presenter.GmMembershipPresenterImpl
import kotlinx.android.synthetic.main.partial_gm_subscribe_membership_auto_subscribe.*
import kotlinx.android.synthetic.main.partial_gm_subscribe_membership_selected_product.*
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.subscribe.membership.view.activity.GmMembershipInfoActivity
import com.tokopedia.gm.subscribe.membership.view.activity.GmMembershipProductActivity


class GmMembershipFragment : BaseDaggerFragment(), GmMembershipView {

    @Inject
    lateinit var presenter: GmMembershipPresenterImpl

    private var subscriptionTypeSelected = DEFAULT_SUBSCRIPTION_TYPE

    private val tvSave : TextView by lazy { activity?.findViewById(R.id.tvSave) as TextView}

    override fun initInjector() {
        getComponent(GmSubscribeMembershipComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String {
        return getString(R.string.gm_membership_title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gm_subscribe_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSave.run {
            text = getString(R.string.save)
            setOnClickListener {
                if(!swAutoExtend.isChecked) subscriptionTypeSelected = DEFAULT_SUBSCRIPTION_TYPE
                presenter.setMembershipData(subscriptionTypeSelected)
            }}


        swAutoExtend.setOnCheckedChangeListener { _, bool ->
            showAutoExtendLayout(bool)
        }

        btnExtend.setOnClickListener { goToProductPage() }

        labelExtendPacket.setOnClickListener { goToProductPage() }

        tvInfoAutoExtend.setOnClickListener {
            goToInfoGmPage()
        }

        initView()
        getMembershipData()
    }

    private fun initView(){
        showAutoExtendLayout(false)
        if(subscriptionTypeSelected == DEFAULT_SUBSCRIPTION_TYPE){
            labelExtendPacket.setContent(getString(R.string.gm_choose_package))
        }
    }

    private fun goToProductPage(){
        context?.run {
            val intent = GmMembershipProductActivity.createIntent(this)
            intent.putExtra(EXTRA_SUBSCRIPTION_TYPE, subscriptionTypeSelected)
            startActivityForResult(intent, EXTRA_MEMBERSHIP_PACKAGE)
        }
    }

    private fun goToInfoGmPage(){
        context?.run {
            val intent = GmMembershipInfoActivity.createIntent(this)
            startActivity(intent)
        }
    }

    private fun getMembershipData(){
        presenter.getMembershipData()
    }

    private fun showAutoExtendLayout(isAutoExtend: Boolean){
        layoutAutoExtend.visibility = if(isAutoExtend) View.VISIBLE else View.GONE
    }

    override fun onSuccessGetGmSubscribeMembershipData(membershipData: GetMembershipData) {
        swAutoExtend.isChecked = (membershipData.auto_extend == 1)

        subscriptionTypeSelected = membershipData.subscription.subscription_type

        val expiredDate = DateFormatUtils.formatDate(FORMAT_DATE_API, DEFAULT_VIEW_FORMAT, membershipData.expired_date)
        val autoWithdrawalDate = DateFormatUtils.formatDate(FORMAT_DATE_API, DEFAULT_VIEW_FORMAT, membershipData.auto_withdrawal_date)
        tvExpiredDate.text = getString(R.string.gmsubscribe_expired_date, expiredDate)
        if(subscriptionTypeSelected != DEFAULT_SUBSCRIPTION_TYPE){
            labelExtendPacket.setContent(membershipData.subscription.product_time_range_fmt + " " + membershipData.subscription.price_fmt)
        }

        tvAutoExtendDate.text = MethodChecker.fromHtml(getString(R.string.gmsubscribe_membership_auto_extend_date, expiredDate))
        val withdrawalDate = "$autoWithdrawalDate - $expiredDate"
        tvWitdhrawalInfo.text = MethodChecker.fromHtml(getString(R.string.gmsubscribe_auto_extend_withdrawal, withdrawalDate))
    }

    override fun onErrorGetGmSubscribeMembershipData(throwable: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(context, throwable))
    }

    override fun onSuccessSetGmSubscribeMembershipData() {
        activity?.finish()
    }

    override fun onErrorSetGmSubscribeMembershipData(throwable: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(context, throwable))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EXTRA_MEMBERSHIP_PACKAGE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getIntExtra(EXTRA_SUBSCRIPTION_TYPE, 0)?.run {
                    subscriptionTypeSelected = this
                }
                var packageName = ""
                data?.getStringExtra(EXTRA_SUBSCRIPTION_NAME)?.run {
                    packageName += this
                }
                data?.getStringExtra(EXTRA_SUBSCRIPTION_PRICE)?.run {
                    packageName = packageName + " " + this
                }
                if(packageName.isNotEmpty())
                    labelExtendPacket.setContent(packageName)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun newInstance() = GmMembershipFragment()
        const val DEFAULT_SUBSCRIPTION_TYPE = 0
        private const val FORMAT_DATE_API = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"

        private const val EXTRA_MEMBERSHIP_PACKAGE = 200

        const val EXTRA_SUBSCRIPTION_TYPE = "EXTRA_SUBSCRIPTION_TYPE"
        const val EXTRA_SUBSCRIPTION_NAME = "EXTRA_SUBSCRIPTION_NAME"
        const val EXTRA_SUBSCRIPTION_PRICE = "EXTRA_SUBSCRIPTION_PRICE"

    }
}