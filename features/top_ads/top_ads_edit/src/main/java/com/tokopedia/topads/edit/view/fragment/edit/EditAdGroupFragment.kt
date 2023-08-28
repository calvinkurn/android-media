package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.databinding.TopadsEditFragmentEditAdGroupBinding
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.adapter.edit.EditAdGroupAdapter
import com.tokopedia.topads.edit.view.adapter.edit.EditAdGroupTypeFactory
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemState
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemUiModel
import com.tokopedia.topads.edit.view.sheet.EditAdGroupDailyBudgetBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupNameBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupRecommendationBidBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupSettingModeBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditAdGroupFragment: BaseDaggerFragment(){

    private var binding by autoClearedNullable<TopadsEditFragmentEditAdGroupBinding>()
    private val editAdGroupAdapter by lazy {
        EditAdGroupAdapter(EditAdGroupTypeFactory())
    }

    private fun getList(context: Context): MutableList<Visitable<*>> {
        return mutableListOf(
            EditAdGroupItemUiModel("Nama Grup", "Group", "") { openAdGroupNameBottomSheet() },
            EditAdGroupItemUiModel("Produk", "6 Produk baru", "") {  },
            EditAdGroupItemUiModel("Mode Pengaturan", "6 Produk baru", "") { openAdGroupSettingModeBottomSheet() },
            EditAdGroupItemUiModel("Iklan di Pencarian", "6 Produk baru", "") {  },
            EditAdGroupItemUiModel("Iklan di Rekomendasi", "6 Produk baru", "") { openAdGroupRecommendationBidBottomSheet() },
            EditAdGroupItemUiModel("Anggaran Harian", "6 Produk baru", "") { openAdGroupDailyBudgetBottomSheet() },
            EditAdGroupItemAdsPotentialUiModel(
                "Potensi tampil",
                context.getString(R.string.footer_potential_widget_edit_ad_group_text),
                "",
                potentialWidgetList, EditAdGroupItemState.LOADING
            )
        )
    }

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopadsEditFragmentEditAdGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = editAdGroupAdapter
            activity?.let {
                editAdGroupAdapter.updateList(getList(it))
            }
        }


//        suspend fun delayWithCoroutines(delayMillis: Long) {
//            delay(delayMillis)
//        }
//
//        GlobalScope.launch(Dispatchers.Main) {
//            delayWithCoroutines(2000)
//            editAdGroupAdapter.updatePotentialWidget(potentialWidgetList)
//        }
    }
    private fun openAdGroupNameBottomSheet(){
        EditAdGroupNameBottomSheet.newInstance().show(parentFragmentManager)
    }


    private fun openAdGroupSettingModeBottomSheet(){
        EditAdGroupSettingModeBottomSheet.newInstance().show(parentFragmentManager)
    }
    private fun openAdGroupRecommendationBidBottomSheet(){
        EditAdGroupRecommendationBidBottomSheet.newInstance().show(parentFragmentManager)
    }

    private fun openAdGroupDailyBudgetBottomSheet(){
        EditAdGroupDailyBudgetBottomSheet.newInstance().show(parentFragmentManager)
    }

    companion object {
        fun newInstance(): EditAdGroupFragment {
            return EditAdGroupFragment()
        }

        private val potentialWidgetList: MutableList<EditAdGroupItemAdsPotentialWidgetUiModel> = mutableListOf(
            EditAdGroupItemAdsPotentialWidgetUiModel("Di Pencarian", "400x/minggu", "+12% meningkat"),
            EditAdGroupItemAdsPotentialWidgetUiModel("Di Rekomendasi", "400x/minggu", "+12% meningkat"),
            EditAdGroupItemAdsPotentialWidgetUiModel("Total Tampil", "800x/minggu", "+12% meningkat")
        )
    }
}
