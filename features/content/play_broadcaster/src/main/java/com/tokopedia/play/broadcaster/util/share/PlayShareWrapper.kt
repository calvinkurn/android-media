package com.tokopedia.play.broadcaster.util.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.requests.LinkerShareRequest
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.play.broadcaster.ui.model.ShareUiModel
import com.tokopedia.url.TokopediaUrl


/**
 * Created by mzennis on 08/06/20.
 */
object PlayShareWrapper {

    private val channelListWebUrl= "${TokopediaUrl.getInstance().WEB}play/channel/"

    fun copyToClipboard(context: Context, shareContents: String, onUrlCopied: () -> Unit) {
        val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("play-broadcaster", shareContents)
        clipboard.setPrimaryClip(clip)
        onUrlCopied()
    }

    fun generateShareLink(shareUiModel: ShareUiModel, onUrlCreated: (String) -> Unit) {
        val defaultShareContent = defaultSharedContent(
                shareContents = shareUiModel.description,
                shareLink = shareUiModel.redirectUrl)

        if (shareUiModel.shortenUrl) {
            generateShortUrl(shareUiModel) { shareLink ->
                onUrlCreated(generateSharedContent(
                        shareContents = shareUiModel.textContent,
                        shareLink = shareLink)?: defaultShareContent)
            }
        }
        else {
            onUrlCreated(generateSharedContent(
                    shareContents = shareUiModel.textContent,
                    shareLink = shareUiModel.redirectUrl)?: defaultShareContent)
        }
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

    private fun generateSharedContent(shareContents: String, shareLink: String): String? = try {
        shareContents.replace("${'$'}{url}", shareLink)
    } catch (e: Exception) { null }

    private fun defaultSharedContent(shareContents: String, shareLink: String): String = String.format("%s\n\n%s", shareContents, shareLink)

    private fun generateShareData(shareData: ShareUiModel): LinkerData {
        val desktopUrl = "$channelListWebUrl${shareData.id}"
        return LinkerData.Builder.getLinkerBuilder()
                .setId(shareData.id)
                .setName(shareData.title)
                .setTextContent(shareData.description)
                .setDescription(shareData.description)
                .setImgUri(shareData.imageUrl)
                .setOgImageUrl(shareData.imageUrl)
                .setOgTitle(shareData.title)
                .setUri(shareData.redirectUrl)
                .setDesktopUrl(desktopUrl)
                .setShareUrl(shareData.redirectUrl)
                .setType(LinkerData.PLAY_BROADCASTER)
                .build()
    }
}