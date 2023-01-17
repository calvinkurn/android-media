package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.TopAdsProductResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.create.databinding.FragmentMpCreateAdGroupBinding
import com.tokopedia.topads.view.sheet.MpCreateGroupBudgetHelpSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.model.MpAdsCreateGroupViewModel
import javax.inject.Inject

private const val GROUP_DETAIL_PAGE = "android.group_detail"

class MpCreateAdGroupFragment : BaseDaggerFragment() {

    private lateinit var binding : FragmentMpCreateAdGroupBinding

    private val successImageUrl = "https://images.tokopedia.net/img/android/topads/createads_success/mp_group_creation_success_dialog.png"

    companion object {
        fun newInstance(): MpCreateAdGroupFragment {
            return MpCreateAdGroupFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val createGroupViewModel : MpAdsCreateGroupViewModel by lazy {
        ViewModelProvider(this,viewModelFactory).get(MpAdsCreateGroupViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMpCreateAdGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupToolbar()
        attachClickListeners()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    private fun init(){
        binding.btnSubmit.isLoading = true
        val suggestions = java.util.ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("", listOf()))
        createGroupViewModel.getBidInfo(suggestions, GROUP_DETAIL_PAGE,  this::onSuccessBidSuggestion)
        createGroupViewModel.getProduct("2150857075", this::onSuccessNameSuggestion)
    }

    private fun onSuccessBidSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            binding.dailyBudget.editText.text = Editable.Factory().newEditable(it.minDailyBudget.toString())
        }
    }

    private fun onSuccessNameSuggestion(response : TopAdsProductResponse){
        response.data?.product.let {
            binding.groupName.editText.text = Editable.Factory().newEditable(it?.productName)
        }
    }

    private fun setupToolbar(){
        binding.headerToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun attachClickListeners(){

        binding.dailyBudget.iconContainer.setOnClickListener{
            MpCreateGroupBudgetHelpSheet().show(childFragmentManager, MpCreateAdGroupFragment::class.java.name)
        }

        binding.btnSubmit.setOnClickListener{

        }
    }

    private fun openSuccessDialog(){
        var dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.setImageUrl(successImageUrl)
        dialog.setDescription(getString(R.string.success_dailog_description))
        dialog.setTitle(getString(R.string.product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(R.string.manage_ads_group))
        dialog.setSecondaryCTAText(getString(R.string.stay_here))
        dialog.setPrimaryCTAClickListener {

        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openInsufficientCreditsDialog(){
        var dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(getString(R.string.success_group_creation_insufficient_credits_text))
        dialog.setTitle(getString(R.string.ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(R.string.add_credit))
        dialog.setSecondaryCTAText(getString(R.string.later))
        dialog.setPrimaryCTAClickListener {

        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
