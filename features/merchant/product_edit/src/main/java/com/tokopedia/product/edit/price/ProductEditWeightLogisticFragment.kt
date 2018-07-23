package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import kotlinx.android.synthetic.main.fragment_product_edit_weightlogistic.*

class ProductEditWeightLogisticFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_weightlogistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        labelSwitchPreOrder.setOnClickListener {
            labelSwitchPreOrder.isChecked = !labelSwitchPreOrder.isChecked
            if(labelSwitchPreOrder.isChecked)
                layoutProcessTime.visibility = View.VISIBLE
            else
                layoutProcessTime.visibility = View.GONE
        }
    }

    companion object {

        fun createInstance(): Fragment {
            return ProductEditWeightLogisticFragment()
        }
    }
}
