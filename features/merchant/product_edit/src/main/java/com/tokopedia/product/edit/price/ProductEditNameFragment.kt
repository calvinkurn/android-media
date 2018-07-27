package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.price.viewholder.ProductEditNameViewHolder
import android.widget.TextView


class ProductEditNameFragment : Fragment(), ProductEditNameViewHolder.Listener {

    private var productName = ProductName()
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        productName = activity!!.intent.getParcelableExtra(EXTRA_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditNameViewHolder(view, this).setName(productName.name!!)
        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
    }

    override fun onNameChanged(productName: ProductName) {
        if (productName.name!!.isNotEmpty()) {
            texViewMenu.setTextColor(ContextCompat.getColor(texViewMenu.context, R.color.tkpd_main_green))
        } else {
            texViewMenu.setTextColor(ContextCompat.getColor(texViewMenu.context, R.color.font_black_secondary_54))
        }
        this.productName = productName
    }

    private fun isDataValid(): Boolean{
        return productName.name!!.isNotEmpty()
    }

    private fun setResult(){
        if(isDataValid()) {
            val intent = Intent()
            intent.putExtra(EXTRA_NAME, productName)
            activity!!.setResult(Activity.RESULT_OK, intent)
            activity!!.finish()
        }
    }

    companion object {
        fun createInstance() = ProductEditNameFragment()
    }
}
