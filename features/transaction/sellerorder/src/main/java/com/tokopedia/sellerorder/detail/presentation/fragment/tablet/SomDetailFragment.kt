package com.tokopedia.sellerorder.detail.presentation.fragment.tablet

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailComponent
import kotlinx.android.synthetic.main.fragment_som_detail.*

class SomDetailFragment : com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment(), Toolbar.OnMenuItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailFragment {
            return SomDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(SomConsts.PARAM_ORDER_ID, bundle.getString(SomConsts.PARAM_ORDER_ID))
                }
            }
        }
    }

    override fun initInjector() {
        activity?.application?.let {
            DaggerSomDetailComponent.builder()
                    .somComponent(SomComponentInstance.getSomComponent(it))
                    .build()
                    .inject(this)
        }
    }

    override fun showBackButton(): Boolean = false

    fun setOrderIdToShow(orderId: String) {
        if (orderId != this.orderId) {
            this.orderId = orderId
            loadDetail()
        }
    }
}