package com.tokopedia.talk.feature.sellersettings.template.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tokopedia.header.HeaderUnify
import com.tokopedia.talk.R

class TalkEditTemplateFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        if(isEditMode()) {
            toolbar?.setTitle(R.string.title_edit_template_page)
            return
        }
        toolbar?.setTitle(R.string.title_add_template_page)
    }

    private fun isEditMode(): Boolean {
        return arguments?.let { TalkEditTemplateFragmentArgs.fromBundle(it).isEdit } ?: false
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

}