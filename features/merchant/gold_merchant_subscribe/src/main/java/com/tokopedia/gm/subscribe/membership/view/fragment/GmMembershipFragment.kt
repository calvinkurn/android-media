package com.tokopedia.gm.subscribe.membership.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.di.GmSubscribeMembershipComponent
import com.tokopedia.gm.subscribe.membership.view.presenter.GmMembershipPresenterImpl
import kotlinx.android.synthetic.main.partial_gm_subscribe_membership_auto_subscribe.*
import kotlinx.android.synthetic.main.partial_gm_subscribe_membership_selected_product.*
import javax.inject.Inject

class GmMembershipFragment : BaseDaggerFragment(), GmMembershipView {

    @Inject
    lateinit var presenter: GmMembershipPresenterImpl

    val tvSave : TextView by lazy { activity?.findViewById(R.id.tvSave) as TextView}

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
        tvExpiredDate.text = "Berlaku sampai 6 Mei 2010"

        swAutoExtend.setOnCheckedChangeListener { _, bool ->
            showAutoExtendLayout(bool)
        }

        labelExtendPacket.setContent("Berlaku sampai 6 Mei 2010")
        tvSave.setOnClickListener {  }

        renderData()
        getMembershipData()
    }

    private fun getMembershipData(){
        presenter.getMembershipData()
    }

    private fun renderData(){
        showAutoExtendLayout(false)
    }

    private fun showAutoExtendLayout(isAutoExtend: Boolean){
        layoutAutoExtend.visibility = if(isAutoExtend) View.VISIBLE else View.GONE
    }

    override fun showProgressDialog() {

    }

    override fun dismissProgressDialog() {

    }

    override fun showMessageError(string: String?) {

    }

    companion object {
        fun newInstance() = GmMembershipFragment()
    }
}