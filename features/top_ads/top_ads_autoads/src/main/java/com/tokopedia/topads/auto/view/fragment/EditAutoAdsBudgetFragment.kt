package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.sheet.AutoAdsCreateSheet
import com.tokopedia.topads.auto.view.widget.AutoAdsWidget
import kotlinx.android.synthetic.main.topads_autoads_edit_daily_budget.*

/**
 * Author errysuprayogi on 09,May,2019
 */
class EditAutoAdsBudgetFragment : AutoAdsBaseBudgetFragment(), View.OnClickListener {
    private var autoAdsWidget: AutoAdsWidget? = null
    private val EDIT_AUTOADS = 1

    override fun getLayoutId(): Int {
        return R.layout.topads_autoads_edit_daily_budget
    }

    override fun setUpView(view: View) {
        autoAdsWidget = view.findViewById(R.id.autoads_edit_widget)
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
        btn_submit.isEnabled = false
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
        btn_submit.isEnabled = true
    }

    override fun setListener() {
        btnSubmit.setOnClickListener(this)
        tipBtn.setOnClickListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoAdsWidget?.loadData(EDIT_AUTOADS)
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return EditAutoAdsBudgetFragment::class.java.name
    }

    companion object {

        fun newInstance(): EditAutoAdsBudgetFragment {
            val args = Bundle()
            val fragment = EditAutoAdsBudgetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_submit) {
            activatedAds()
        }
        if (v?.id == R.id.tip_btn) {
            AutoAdsCreateSheet.newInstance(context!!).show()
        }
    }

}
