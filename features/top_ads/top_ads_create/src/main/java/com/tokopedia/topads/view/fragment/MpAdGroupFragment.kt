package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.create.databinding.MpAdGroupFragmentBinding
import com.tokopedia.topads.view.adapter.adgrouplist.AdGroupListAdapter
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactoryImpl
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.AdGroupViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify

class MpAdGroupFragment : BaseDaggerFragment(),AdGroupViewHolder.AdGroupListener {

    companion object{
        fun newInstance() : MpAdGroupFragment{
            return MpAdGroupFragment()
        }
    }

    private var binding:MpAdGroupFragmentBinding?=null

    private val adGroupAdapter:AdGroupListAdapter by lazy {
        AdGroupListAdapter(getAdGroupTypeFactory())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MpAdGroupFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupRecyclerView()
        setupDummyList()
    }

    private fun getAdGroupTypeFactory() : AdGroupTypeFactory{
        return AdGroupTypeFactoryImpl(this)
    }

    private fun setupHeader(){
      binding?.adGroupHeader?.apply {
          setNavigationOnClickListener {
          activity?.onBackPressed()
          }
      }
    }

    private fun setupRecyclerView(){
        binding?.adGroupRv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adGroupAdapter
        }
    }

    private fun setupDummyList(){
        val list : List<Visitable<Any>> = listOf(
            CreateAdGroupUiModel(),
            AdGroupUiModel(),
            AdGroupUiModel(),
            AdGroupUiModel(),
            AdGroupUiModel(),
            AdGroupUiModel(),
            AdGroupUiModel(),
            ReloadInfiniteUiModel()
        ) as List<Visitable<Any>>
        adGroupAdapter.submitList(list)
    }

    override fun onAdStatClicked(bottomSheet: BottomSheetUnify) {
        bottomSheet.show(childFragmentManager,"")
    }

    override fun getScreenName() = ""
    override fun initInjector() {}

}
