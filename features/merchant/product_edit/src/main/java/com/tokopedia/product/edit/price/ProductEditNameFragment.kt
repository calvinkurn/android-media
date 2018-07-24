package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.viewholder.ProductEditNameViewHolder

class ProductEditNameFragment : Fragment(), ProductEditNameViewHolder.Listener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditNameViewHolder(view, this)
    }

    override fun onNameChanged(name: String) {

    }

    companion object {

        fun createInstance(): Fragment {
            return ProductEditNameFragment()
        }
    }
}
