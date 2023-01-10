package com.tokopedia.topads.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.create.databinding.FragmentMpCreateAdGroupBinding
import com.tokopedia.topads.view.sheet.MpCreateGroupBudgetHelpSheet
import com.tokopedia.topads.create.R

class MpCreateAdGroupFragment : Fragment() {

    private lateinit var binding : FragmentMpCreateAdGroupBinding

    private val successImageUrl = "https://images.tokopedia.net/img/android/topads/createads_success/mp_group_creation_success_dialog.png"

    companion object {
        fun newInstance(): MpCreateAdGroupFragment {
            return MpCreateAdGroupFragment()
        }
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
        setupToolbar()
        attachClickListeners()
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
            openSuccessDialog()
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
