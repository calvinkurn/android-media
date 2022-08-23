package com.tokopedia.home_account.explicitprofile.features.popupinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.LayoutExplicitBottomSheetSectionsInfoBinding
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SectionInfoBottomSheet: BottomSheetUnify() {

    private var viewBinding by autoClearedNullable<LayoutExplicitBottomSheetSectionsInfoBinding>()
    private var sectionAdapter: SectionInfoAdapter? = SectionInfoAdapter()
    private val sectionDataModel: SectionsDataModel? by lazy {
        arguments?.getParcelable(KEY_SECTIONS_DATA) ?: SectionsDataModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = LayoutExplicitBottomSheetSectionsInfoBinding
            .inflate(LayoutInflater.from(context))
            .apply {
                setChild(root)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setTitle(getString(R.string.explicit_profile_info))

        viewBinding?.sectionQuestionList?.apply {
            adapter = sectionAdapter
        }

        sectionDataModel?.questions?.let {
            sectionAdapter?.apply {
                addItems(it)
                notifyDataSetChanged()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "BottomSheetQuestionsInfo"
        private const val KEY_SECTIONS_DATA = "keyQuestionsData"

        @JvmStatic
        fun createInstance(sectionsDataModel: SectionsDataModel) : SectionInfoBottomSheet {
            return SectionInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_SECTIONS_DATA, sectionsDataModel)
                }
            }
        }
    }
}