package com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEduCenterBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class EduCenterBottomSheet: BottomSheetUnify() {

    companion object {
        fun createInstance() = EduCenterBottomSheet()
        private val TAG = EduCenterBottomSheet::class.java.simpleName
    }

    private var eduCenterMenuAdapter: EduCenterMenuAdapter? = null

    private var binding by autoClearedNullable<SmvcBottomsheetEduCenterBinding>()

    fun initRecyclerView(context: Context, listener: EduCenterClickListener){
        eduCenterMenuAdapter = EduCenterMenuAdapter(context, listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@EduCenterBottomSheet)?.commit()
        }
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetEduCenterBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_bottomsheet_edu_center_title))
    }

    private fun setupView() {
        binding?.menuList?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = eduCenterMenuAdapter
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

}
