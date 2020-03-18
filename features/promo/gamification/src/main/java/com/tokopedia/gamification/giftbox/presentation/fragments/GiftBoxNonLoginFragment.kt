package com.tokopedia.gamification.giftbox.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.gamification.R


class GiftBoxNonLoginFragment : Fragment() {

    lateinit var btnCreateAccount: View
    lateinit var btnLogin: View

    fun getLayout() = R.layout.fragment_gift_box_non_login

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(context).inflate(getLayout(), container, false)

        btnCreateAccount = v.findViewById(R.id.btnCreateAccount)
        btnLogin = v.findViewById(R.id.btnLogin)

        return v
    }
}