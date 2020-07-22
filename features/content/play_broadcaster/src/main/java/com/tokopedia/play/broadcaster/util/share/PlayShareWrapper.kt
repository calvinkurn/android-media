package com.tokopedia.play.broadcaster.util.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.requests.LinkerShareRequest
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.play.broadcaster.ui.model.ShareUiModel


/**
 * Created by mzennis on 08/06/20.
 */
object PlayShareWrapper {

    fun doCopyShareLink(context: Context, shareData: ShareUiModel, onUrlCopied: () -> Unit) {
        if (shareData.shortenUrl) generateShortUrl(shareData) { shortenUrl ->
            val shareContents =
                    generateSharedContent(shareData.textContent, shortenUrl) ?:
                    defaultSharedContent(shareData.description, shareData.redirectUrl)
            doCopyToClipboard(context, shareContents, onUrlCopied)
        } else {
            val shareContents =
                    generateSharedContent(shareData.textContent, shareData.redirectUrl) ?:
                    defaultSharedContent(shareData.description, shareData.redirectUrl)
            doCopyToClipboard(context, shareContents, onUrlCopied)
        }
    }

    private fun doCopyToClipboard(context: Context, shareContents: String, onUrlCopied: () -> Unit) {
        val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("play-broadcaster", shareContents)
        clipboard.primaryClip = clip
        onUrlCopied()
    }

    private fun generateShortUrl(shareData: ShareUiModel, onUrlCreated: (String) -> Unit) {
        try {
            LinkerManager.getInstance().executeShareRequest(LinkerShareRequest(0,
                    DataMapper.getLinkerShareData(generateShareData(shareData)), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                   onUrlCreated(linkerShareData?.url?:shareData.redirectUrl)
                }

                override fun onError(linkerError: LinkerError?) {
                    onUrlCreated(shareData.redirectUrl)
                }
            }))
        } catch (e: Exception) {
            onUrlCreated(shareData.redirectUrl)
        }
    }

    private fun generateSharedContent(text: String, url: String): String? = try {
        text.replace("${'$'}{url}", url)
    } catch (e: Exception) { null }

    private fun defaultSharedContent(shareContents: String, shareLink: String): String = String.format("%s\n\n%s", shareContents, shareLink)

    private fun generateShareData(shareData: ShareUiModel): LinkerData = LinkerData.Builder.getLinkerBuilder()
            .setId(shareData.id)
            .setName(shareData.title)
            .setTextContent(shareData.description)
            .setDescription(shareData.description)
            .setImgUri(shareData.imageUrl)
            .setOgImageUrl(shareData.imageUrl)
            .setOgTitle(shareData.title)
            .setUri(Uri.parse(shareData.redirectUrl).toString())
            .setShareUrl(shareData.redirectUrl)
            .setType(LinkerData.APP_SHARE_TYPE)
            .build()
}