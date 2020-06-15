package com.tokopedia.play.broadcaster.util

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


/**
 * Created by mzennis on 08/06/20.
 */
object PlayShareWrapper {

    fun doCopyShareLink(context: Context, shareUiModel: ShareUiModel, onUrlCopied: () -> Unit) {
        // TODO("try catch for testing only, because testapp does not init LinkerManager")
        try {
            LinkerManager.getInstance().executeShareRequest(LinkerShareRequest(0,
                    DataMapper.getLinkerShareData(generateShareData(shareUiModel)), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareContents = linkerShareData?.shareContents?:
                    generateShareContents(shareUiModel.description, shareUiModel.redirectUrl)
                    doCopyToClipboard(context, shareContents, onUrlCopied)
                }

                override fun onError(linkerError: LinkerError?) {
                    doCopyToClipboard(
                            context,
                            generateShareContents(shareUiModel.description, shareUiModel.redirectUrl),
                            onUrlCopied
                    )
                }
            }))
        } catch (e: Exception) {
            doCopyToClipboard(
                    context,
                    generateShareContents(shareUiModel.description, shareUiModel.redirectUrl),
                    onUrlCopied
            )
        }
    }

    private fun doCopyToClipboard(context: Context, shareContents: String, onUrlCopied: () -> Unit) {
        val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", shareContents)
        clipboard.primaryClip = clip
        onUrlCopied()
    }

    private fun generateShareContents(shareContents: String, shareLink: String): String = String.format("%s\n\n%s", shareContents, shareLink)

    private fun generateShareData(shareUiModel: ShareUiModel): LinkerData = LinkerData.Builder.getLinkerBuilder()
            .setId(shareUiModel.id)
            .setName(shareUiModel.title)
            .setTextContent(shareUiModel.description)
            .setDescription(shareUiModel.description)
            .setImgUri(shareUiModel.imageUrl)
            .setOgImageUrl(shareUiModel.imageUrl)
            .setOgTitle(shareUiModel.title)
            .setUri(shareUiModel.redirectUrl)
            .setType(LinkerData.GROUPCHAT_TYPE)
            .build()
}