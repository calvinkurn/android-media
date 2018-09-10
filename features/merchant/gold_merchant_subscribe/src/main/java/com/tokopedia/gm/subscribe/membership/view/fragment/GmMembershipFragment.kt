package com.tokopedia.gm.subscribe.membership.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.core.util.DateFormatUtils
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.data.model.MembershipData
import com.tokopedia.gm.subscribe.membership.di.GmSubscribeMembershipComponent
import com.tokopedia.gm.subscribe.membership.view.presenter.GmMembershipPresenterImpl
import kotlinx.android.synthetic.main.partial_gm_subscribe_membership_auto_subscribe.*
import kotlinx.android.synthetic.main.partial_gm_subscribe_membership_selected_product.*
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.subscribe.view.activity.GmProductActivity


class GmMembershipFragment : BaseDaggerFragment(), GmMembershipView {

    @Inject
    lateinit var presenter: GmMembershipPresenterImpl

    private var subscriptionTypeSelected = 0

    private val FORMAT_DATE_API = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
    private val SELECT_AUTO_SUBSCRIBE_PRODUCT = 200
    private val CHANGE_AUTO_SUBSCRIBE_PRODUCT = 300

    private val tvSave : TextView by lazy { activity?.findViewById(R.id.tvSave) as TextView}

    override fun initInjector() {
        getComponent(GmSubscribeMembershipComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String {
        return "test"
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
                presenter.setMembershipData(subscriptionTypeSelected)
            }}


        swAutoExtend.setOnCheckedChangeListener { _, bool ->
            showAutoExtendLayout(bool)
        }

        labelExtendPacket.setOnClickListener {
            context?.run {
                val intent = GmProductActivity.selectAutoProductFirstTime(this)
                startActivityForResult(intent, SELECT_AUTO_SUBSCRIBE_PRODUCT)
            }
        }
        tvInfoAutoExtend.setOnClickListener {  }

        showAutoExtendLayout(false)
        getMembershipData()
    }

    private fun getMembershipData(){
        presenter.getMembershipData()
    }

    private fun showAutoExtendLayout(isAutoExtend: Boolean){
        layoutAutoExtend.visibility = if(isAutoExtend) View.VISIBLE else View.GONE
    }

    override fun onSuccessGetGmSubscribeMembershipData(membershipData: MembershipData) {
        swAutoExtend.isChecked = (membershipData.auto_extend == 1)

        subscriptionTypeSelected = membershipData.subscription.subscription_type

        val expiredDate = DateFormatUtils.formatDate(FORMAT_DATE_API, DEFAULT_VIEW_FORMAT, membershipData.expired_date)
        val autoWithdrawalDate = DateFormatUtils.formatDate(FORMAT_DATE_API, DEFAULT_VIEW_FORMAT, membershipData.auto_withdrawal_date)
        tvExpiredDate.text = getString(R.string.gmsubscribe_expired_date, expiredDate)
        labelExtendPacket.setContent(membershipData.subscription.name)

        tvAutoExtendDate.text = MethodChecker.fromHtml(getString(R.string.gmsubscribe_membership_auto_extend_date, expiredDate))
        val withdrawalDate = "$autoWithdrawalDate - $expiredDate"
        tvWitdhrawalInfo.text = MethodChecker.fromHtml(getString(R.string.gmsubscribe_auto_extend_withdrawal, withdrawalDate))
    }

    override fun onErrorGetGmSubscribeMembershipData(error: String) {

    }

    override fun onSuccessSetGmSubscribeMembershipData() {
        activity?.finish()
    }

    override fun onErrorSetGmSubscribeMembershipData(error: String) {

    }

    override fun showProgressDialog() {

    }

    override fun dismissProgressDialog() {

    }

    companion object {
        fun newInstance() = GmMembershipFragment()
    }
}