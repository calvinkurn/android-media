package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.common.Utils
import com.tokopedia.sellerpersona.databinding.FragmentPersonaResultBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaSimpleListAdapter
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class PersonaResultFragment : BaseFragment<FragmentPersonaResultBinding>() {

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun bind(
        layoutInflater: LayoutInflater, container: ViewGroup?
    ): FragmentPersonaResultBinding {
        return FragmentPersonaResultBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }

    private fun setupView() {
        binding?.run {
            showShopAvatar()
            setupCheckListItem()

            val hexColor = Utils.getHexColor(
                root.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            tvSpSelectManualType.text = root.context.getString(
                R.string.sp_persona_result_select_manual, hexColor
            ).parseAsHtml()
            imgSpResultBackdrop.loadImage(Constants.IMG_BACKGROUND_RUMAHAN)
            imgSpResultAvatar.loadImage(Constants.IMG_AVATAR_RUMAHAN)
            btnSpRetryQuestionnaire.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.actionResultFragmentToQuestionnaireFragment)
            }
            tvSpSelectManualType.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.actionResultFragmentToSelectTyoeFragment)
            }
        }
    }

    private fun showShopAvatar() {
        binding?.imgSpResultAvatar?.loadImage(userSession.shopAvatar)
    }

    private fun setupCheckListItem() {
        val items = listOf(
            "Menerima 1-10 pesanan per hari",
            "Sering mencari peluang untuk strategi baru bisnismu",
            "Punya pegawai yang mengurus operasional toko",
            "Punya toko fisik (offline)",
            "Mengakses Tokopedia Seller di desktop dan aplikasi HP"
        )
        binding?.rvSpResultInfoList?.adapter = PersonaSimpleListAdapter(items)
    }
}