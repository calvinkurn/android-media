package com.tokopedia.loginregister.external_register.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.listener.BaseFinalListener
import com.tokopedia.loginregister.external_register.ovo.analytics.OvoCreationAnalytics
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.fragment_base_success_external_account.view.*
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
        view.fragment_base_success_main_btn?.setOnClickListener {
            listener?.onMainSuccessButtonClicked()
        }
    }

    fun setMainImage(imgResId: Int = 0, imgUrl: String = ""){
        if(imgResId != 0){
            view?.fragment_base_success_main_img?.setImageResource(imgResId)
        } else if(imgUrl.isNotEmpty()){
            view?.run {
                ImageUtils.loadImageWithoutPlaceholderAndError(fragment_base_success_main_img, imgUrl)
            }
        }
    }

    fun setTitle(title: String) {
        view?.fragment_base_success_title?.text = title
    }

    fun setDescription(description: String){
        view?.fragment_base_success_description?.text = description
    }

    fun setButtonText(text: String) {
        view?.fragment_base_success_main_btn?.text = text
    }

    fun setSuccessListener(listener: BaseFinalListener) {
        this.listener = listener
    }
}