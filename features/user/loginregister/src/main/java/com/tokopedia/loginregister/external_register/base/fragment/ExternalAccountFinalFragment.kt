package com.tokopedia.loginregister.external_register.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentBaseSuccessExternalAccountBinding
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.listener.BaseFinalListener
import com.tokopedia.loginregister.external_register.ovo.analytics.OvoCreationAnalytics
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

open class ExternalAccountFinalFragment: BaseDaggerFragment() {

     var listener: BaseFinalListener? = null

    @Inject
    lateinit var ovoCreationAnalytics: OvoCreationAnalytics

    @Inject
    lateinit var externalRegisterPreference: ExternalRegisterPreference
    override fun getScreenName(): String = ""

    private val binding: FragmentBaseSuccessExternalAccountBinding? by viewBinding()

    override fun initInjector() {
        getComponent(ExternalRegisterComponent::class.java).inject(this)
    }

    companion object {
        fun createInstance(): ExternalAccountFinalFragment {
            return ExternalAccountFinalFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_success_external_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.fragmentBaseSuccessMainBtn?.setOnClickListener {
            listener?.onMainSuccessButtonClicked()
        }
    }

    fun setMainImage(imgResId: Int = 0, imgUrl: String = ""){
        if(imgResId != 0){
            binding?.fragmentBaseSuccessMainImg?.setImageResource(imgResId)
        } else if(imgUrl.isNotEmpty()){
            binding?.fragmentBaseSuccessMainImg?.let { mainImg ->
                view?.run {
                    ImageUtils.loadImageWithoutPlaceholderAndError(mainImg, imgUrl)
                }
            }
        }
    }

    fun setTitle(title: String) {
        binding?.fragmentBaseSuccessTitle?.text = title
    }

    fun setDescription(description: String){
        binding?.fragmentBaseSuccessDescription?.text = description
    }

    fun setButtonText(text: String) {
        binding?.fragmentBaseSuccessMainBtn?.text = text
    }

    fun setSuccessListener(listener: BaseFinalListener) {
        this.listener = listener
    }
}