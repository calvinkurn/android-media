package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.fragment_choose_address.*

class AddressListFragment : BaseDaggerFragment() {

    val adapter = AddressListItemAdapter()

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_save_address.setOnClickListener {
            if(empty_state_order_list.visibility == View.VISIBLE) {
                address_list_layout.visibility = View.VISIBLE
                empty_state_order_list.visibility = View.GONE
            } else {
                address_list_layout.visibility = View.GONE
                empty_state_order_list.visibility = View.VISIBLE
            }
        }

        address_list_rv.adapter = adapter
        address_list_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


    }

}