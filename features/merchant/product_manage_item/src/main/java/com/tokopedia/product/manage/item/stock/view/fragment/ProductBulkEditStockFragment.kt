package com.tokopedia.product.manage.item.stock.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.manage.item.utils.LabelRadioButton
import kotlinx.android.synthetic.main.fragment_bulk_edit_stock.*

class ProductBulkEditStockFragment : Fragment() {

    private var stockStatus: Boolean = false

    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_bulk_edit_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioButtonAvailable.isChecked = true
        radioButtonAvailable.setOnClickListener {
            setRadioButtonChosen(radioButtonAvailable)
        }

        radioButtonEmpty.setOnClickListener {
            setRadioButtonChosen(radioButtonEmpty)
        }

        texViewMenu?.run {
            text = getString(R.string.label_save)
            setOnClickListener {
                setResult()
            }
        }
    }

    private fun setRadioButtonChosen(labelRadioButton: LabelRadioButton) {
        radioButtonAvailable.isChecked = false
        radioButtonEmpty.isChecked = false
        labelRadioButton.isChecked = true
    }

    private fun saveData(): Boolean {
        stockStatus = !radioButtonEmpty.isChecked
        return stockStatus
    }

    private fun setResult() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_STOCK, saveData()) })
            finish()
        }
    }

    companion object {
        fun createInstance() = ProductBulkEditStockFragment()
    }
}
