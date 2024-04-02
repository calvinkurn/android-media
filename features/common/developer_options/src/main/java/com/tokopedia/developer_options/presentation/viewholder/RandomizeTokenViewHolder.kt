package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.RandomizeAccessTokenUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.EncoderDecoder

class RandomizeTokenViewHolder(
    itemView: View
): AbstractViewHolder<RandomizeAccessTokenUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_randomize_token
    }

    override fun bind(element: RandomizeAccessTokenUiModel) {
        val userSession = UserSession(itemView.context)
        if(userSession.isLoggedIn) {
            val btn = itemView.findViewById<UnifyButton>(R.id.randomize_token_btn)
            btn.setOnClickListener {
                userSession.setToken("abc12345", userSession.tokenType)
            }
        }
    }

    private fun broadcastForceLogoutInfo(context: Context) {
        val intent = Intent()
        intent.action = "com.tokopedia.tkpd.FORCE_LOGOUT_v2"
        intent.putExtra("title", "Kamu otomatis keluar dari akun ini")
        intent.putExtra("description", "Akunmu terhubung ke banyak perangkat. Biar akunmu tetap aman, kamu dikeluarkan dari akun ini dulu. Cek info lengkapnya di Tokopedia Care, ya.")
        intent.putExtra("url", "https://google.com")
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

}
