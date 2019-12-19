package com.tokopedia.discovery.catalogrevamp.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse.ProductCatalogQuery.Data.Catalog.Specification
import com.tokopedia.transaction.orders.common.view.DoubleTextView
import kotlinx.android.synthetic.main.fragment_catalog_specs_and_detail_fragment.*

class CatalogSpecsAndDetailFragment : Fragment() {
    private var type: Int = -1

    companion object {
        const val SPECIFICATION_TYPE = 0
        const val DESCRIPTION_TYPE = 1
        const val TYPE = "TYPE"
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"

        fun newInstance(type: Int, description: String?, specifications: ArrayList<Specification>?): CatalogSpecsAndDetailFragment {
            return CatalogSpecsAndDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE, type)
                    putString(DESCRIPTION, description)
                    putParcelableArrayList(SPECIFICATION, specifications)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_specs_and_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var description: String? = null
        var specifications: ArrayList<Specification>? = null

        if (arguments != null) {
            type = arguments!!.getInt(TYPE, 0)
            description = arguments?.getString(DESCRIPTION)
            specifications = arguments?.getParcelableArrayList(SPECIFICATION)
        }

        if (type == SPECIFICATION_TYPE) {
            if(specifications!=null)
                setSpecificationView(specifications)
        } else {
            setDescriptionView(description)
        }
    }

    private fun setSpecificationView(specifications: ArrayList<Specification>) {
        linear_layout.removeAllViews()
        for(specs in specifications){
            val headerView = TextView(context)
            headerView.text = specs.name
            headerView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f)
            headerView.typeface = Typeface.DEFAULT_BOLD
            headerView.setTextColor(MethodChecker.getColor(context,R.color.grey_796))
            linear_layout.addView(headerView)
            for(row in specs.row){
                val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                doubleTextView.setTopText(MethodChecker.fromHtml(row.key).toString())
                doubleTextView.setTopTextSize(14.0f)
                doubleTextView.setTopTextColor(MethodChecker.getColor(context, R.color.unify_N700_44))
                doubleTextView.setBottomTextSize(14.0f)
                doubleTextView.setBottomTextColor(MethodChecker.getColor(context, R.color.grey_796))
                doubleTextView.setBottomTextStyle("")
                doubleTextView.setBottomText(MethodChecker.fromHtml(row.value.joinToString(",\n")).toString())
                linear_layout.addView(doubleTextView)
            }
            val lineView = View(context)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lineView.minimumHeight = 3
            lineView.setBackgroundColor(MethodChecker.getColor(context, R.color.grey_line))
            params.setMargins(0, resources.getDimensionPixelOffset(R.dimen.dp_16), 0, resources.getDimensionPixelOffset(R.dimen.dp_16))
            lineView.layoutParams = params
            linear_layout.addView(lineView)
        }

    }

    private fun setDescriptionView(description: String?) {
        val headerView = TextView(context)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        headerView.layoutParams = params
        headerView.text = MethodChecker.fromHtml(description).toString()
        headerView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f)
        headerView.setTextColor(MethodChecker.getColor(context,R.color.grey_796))
        linear_layout.addView(headerView)
    }

}