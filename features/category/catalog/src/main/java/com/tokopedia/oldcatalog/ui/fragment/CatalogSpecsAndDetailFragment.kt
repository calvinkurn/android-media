package com.tokopedia.oldcatalog.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.model.raw.FullSpecificationsComponentData
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.DoubleTextView
import kotlinx.android.synthetic.main.fragment_catalog_specs_and_detail_fragment.*

class CatalogSpecsAndDetailFragment : Fragment() {
    private var type: Int = -1
    private var jumpToFullSpecIndex : Int = 0

    companion object {
        const val SPECIFICATION_TYPE = 0
        const val DESCRIPTION_TYPE = 1
        const val TYPE = "TYPE"
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"
        private const val CATALOG_SPECS_TEXT_SIZE = 14.0f
        private const val CATALOG_BOTTOM_LINEAR_WEIGHT = 0.5f
        private const val JUMP_TO_FULL_SPEC_INDEX = "JUMP_TO_FULL_SPEC_INDEX"
        private const val SETTLING_MILLIS = 100L
        fun newInstance(type: Int, description: String?, specifications: ArrayList<FullSpecificationsComponentData>?,
        jumpTo :Int = 0): CatalogSpecsAndDetailFragment {
            return CatalogSpecsAndDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE, type)
                    putString(DESCRIPTION, description)
                    putParcelableArrayList(SPECIFICATION, specifications)
                    putInt(JUMP_TO_FULL_SPEC_INDEX, jumpTo)
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
        var specifications: ArrayList<FullSpecificationsComponentData>? = null

        if (arguments != null) {
            type = requireArguments().getInt(TYPE, 0)
            description = arguments?.getString(DESCRIPTION)
            specifications = arguments?.getParcelableArrayList(SPECIFICATION)
            jumpToFullSpecIndex = arguments?.getInt(JUMP_TO_FULL_SPEC_INDEX) ?: 0
        }

        if (type == SPECIFICATION_TYPE) {
            if(specifications!=null)
                setSpecificationView(specifications)
        } else {
            setDescriptionView(description)
        }
    }

    private fun setSpecificationView(specifications: ArrayList<FullSpecificationsComponentData>) {
        linear_layout.removeAllViews()
        var jumpToView : View? = null
        specifications.forEachIndexed { specsIndex, specs ->
            if(specs.specificationsRow == null){
                return
            }
            context?.let { context ->
                val headerView = Typography(context)
                headerView.apply {
                    text = specs.name
                    setType(Typography.BODY_2)
                    typeface = Typeface.DEFAULT_BOLD
                }
                linear_layout.addView(headerView)
                specs.specificationsRow.forEachIndexed { index, row ->
                    val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                    doubleTextView.apply {
                        setTopText(MethodChecker.fromHtml(row.key).toString())
                        setTopTextSize(CATALOG_SPECS_TEXT_SIZE)
                        setBottomLinearLayoutWeight(CATALOG_BOTTOM_LINEAR_WEIGHT)
                        setTopTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
                        setBottomTextSize(CATALOG_SPECS_TEXT_SIZE)
                        setBottomTextColor(MethodChecker.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                        doubleTextView.setBottomTextStyle("")
                        setBottomText(MethodChecker.fromHtml(row.value))
                    }
                    if(0 == index){
                        doubleTextView.setMainLayoutTopMargin(resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16))
                    }else {
                        doubleTextView.setMainLayoutTopMargin(resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8))
                    }
                    linear_layout.addView(doubleTextView)
                    val lineView = View(context)
                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lineView.minimumHeight = 2
                    lineView.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN50))
                    if(specs.specificationsRow.size - 1 == index){
                        params.setMargins(0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8), 0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_24))
                    }else {
                        params.setMargins(0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8), 0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_0))
                    }
                    lineView.layoutParams = params
                    linear_layout.addView(lineView)
                }
                if(specsIndex == jumpToFullSpecIndex){
                    jumpToView = headerView
                }
            }
        }
        if(jumpToView != null  && jumpToFullSpecIndex != 0){
            specs_scroll_view.postDelayed({
                specs_scroll_view.scrollTo(0, jumpToView?.top ?: 0)
            },SETTLING_MILLIS)
        }
    }

    private fun setDescriptionView(description: String?) {
        context?.let { context ->
            val headerView = Typography(context)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            headerView.apply {
                layoutParams = params
                text = MethodChecker.fromHtml(description).toString()
                setType(Typography.BODY_2)
            }
            linear_layout.addView(headerView)
        }
    }
}
