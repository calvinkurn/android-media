package com.tokopedia.product.manage.item.name.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.*
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.name.view.viewholder.ProductEditNameViewHolder
import android.widget.TextView
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_EDITABLE_NAME


class ProductEditNameFragment : Fragment(), ProductEditNameViewHolder.Listener {

    private var productName = ProductName()
    private var isEditable = true
    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            productName = it.intent.getParcelableExtra(EXTRA_NAME)?: ProductName()
            isEditable = it.intent.getBooleanExtra(EXTRA_IS_EDITABLE_NAME, true)
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_NAME)) {
                productName = savedInstanceState.getParcelable(SAVED_PRODUCT_NAME)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productEditNameViewHolder = ProductEditNameViewHolder(view, this)
        productEditNameViewHolder.setName(productName.name)
        productEditNameViewHolder.setEditableName(isEditable)
        texViewMenu?.run{text = getString(R.string.label_save)
            setOnClickListener {
                setResult()
            }}
    }

    override fun onNameChanged(productName: ProductName) {
        texViewMenu?.run {
            setTextColor(ContextCompat.getColor(context, if (productName.name.isNotEmpty()) R.color.tkpd_main_green
            else R.color.font_black_secondary_54))
        }
        this.productName = productName
    }

    private fun isDataValid(): Boolean{
        return productName.name.isNotEmpty()
    }

    private fun setResult(){
        if(isDataValid()) {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent().apply {putExtra(EXTRA_NAME, productName) })
                finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_NAME, productName)
    }

    companion object {
        const val SAVED_PRODUCT_NAME = "SAVED_PRODUCT_NAME"
        fun createInstance() = ProductEditNameFragment()
    }
}
