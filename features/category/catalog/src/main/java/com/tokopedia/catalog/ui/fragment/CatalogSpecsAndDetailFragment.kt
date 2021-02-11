package com.tokopedia.catalog.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.SpecificationsComponentData
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.DoubleTextView
import kotlinx.android.synthetic.main.fragment_catalog_specs_and_detail_fragment.*

class CatalogSpecsAndDetailFragment : Fragment() {
    private var type: Int = -1

    companion object {
        const val SPECIFICATION_TYPE = 0
        const val DESCRIPTION_TYPE = 1
        const val TYPE = "TYPE"
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"

        fun newInstance(type: Int, description: String?, specifications: ArrayList<SpecificationsComponentData>?): CatalogSpecsAndDetailFragment {
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
        var specifications: ArrayList<SpecificationsComponentData>? = null

        if (arguments != null) {
            type = requireArguments().getInt(TYPE, 0)
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

    private fun setSpecificationView(specifications: ArrayList<SpecificationsComponentData>) {
        linear_layout.removeAllViews()
        for(specs in specifications){
            context?.let { context ->
                val headerView = Typography(context)
                headerView.apply {
                    text = specs.name
                    setType(Typography.BODY_2)
                    typeface = Typeface.DEFAULT_BOLD
                }
                linear_layout.addView(headerView)
                for(row in specs.specificationsRow){
                    val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                    doubleTextView.apply {
                        setTopText(MethodChecker.fromHtml(row.key).toString())
                        setTopTextSize(14.0f)
                        setBottomLinearLayoutWeight(0.5f)
                        setTopTextColor(MethodChecker.getColor(context, R.color.catalog_N700_44))
                        setBottomTextSize(14.0f)
                        setBottomTextColor(MethodChecker.getColor(context, R.color.catalog_grey_796))
                        doubleTextView.setBottomTextStyle("")
                        setBottomText(MethodChecker.fromHtml(row.value.joinToString(",\n")).toString())
                    }
                   linear_layout.addView(doubleTextView)
                }
                val lineView = View(context)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lineView.minimumHeight = 3
                lineView.setBackgroundColor(MethodChecker.getColor(context, R.color.catalog_grey_line))
                params.setMargins(0, resources.getDimensionPixelOffset(R.dimen.dp_16), 0, resources.getDimensionPixelOffset(R.dimen.dp_16))
                lineView.layoutParams = params
                linear_layout.addView(lineView)
            }
        }
    }

    private fun setDescriptionView(description: String?) {
        context?.let { context ->
            val headerView = Typography(context)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            headerView.apply {
                layoutParams = params
                text = MethodChecker.fromHtml(description).toString()
                setType(Typography.BODY_2)
            }
            linear_layout.addView(headerView)
        }
    }
}