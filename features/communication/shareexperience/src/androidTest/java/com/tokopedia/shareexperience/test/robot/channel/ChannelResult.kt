package com.tokopedia.shareexperience.test.robot.channel

import android.content.ClipboardManager
import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasPackage
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.util.ShareExConstants
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertEquals

object ChannelResult {
    fun assertWhatsapp() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.WHATSAPP)
        val typeMatcher = hasType(ShareExMimeTypeEnum.TEXT.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertFbFeed() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.FACEBOOK)
        val typeMatcher = hasType(ShareExMimeTypeEnum.ALL.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertFbStory() {
        val actionMatcher = hasAction(ShareExConstants.IntentAction.FB_STORY)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.FACEBOOK)
        val typeMatcher = hasType(ShareExMimeTypeEnum.IMAGE.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertIgFeed() {
        val actionMatcher = hasAction(ShareExConstants.IntentAction.IG_FEED)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.INSTAGRAM)
        val typeMatcher = hasType(ShareExMimeTypeEnum.IMAGE.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertIgStory() {
        val actionMatcher = hasAction(ShareExConstants.IntentAction.IG_STORY)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.INSTAGRAM)
        val typeMatcher = hasType(ShareExMimeTypeEnum.IMAGE.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertIgDm() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.INSTAGRAM)
        val typeMatcher = hasType(ShareExMimeTypeEnum.TEXT.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertLine() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.LINE)
        val typeMatcher = hasType(ShareExMimeTypeEnum.TEXT.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertTwitter() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.TWITTER)
        val typeMatcher = hasType(ShareExMimeTypeEnum.IMAGE.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertTelegram() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.TELEGRAM)
        val typeMatcher = hasType(ShareExMimeTypeEnum.IMAGE.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertCopyLink(clipboard: ClipboardManager) {
        val expectedText = "https://tokopedia.link/test123"
        val clipData = clipboard.primaryClip
        val copiedText = clipData?.getItemAt(0)?.text.toString()

        assertEquals(expectedText, copiedText)
    }

    fun assertSMS() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage("dummy.sms.package")
        val typeMatcher = hasType(ShareExMimeTypeEnum.ALL.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertEmail() {
        val actionMatcher = hasAction(Intent.ACTION_SEND)
        val extraMatcher = hasExtra(Intent.EXTRA_TEXT, "Halo ada barang bagus loh https://tokopedia.link/test123")
        val packageMatcher = hasPackage(ShareExConstants.PackageName.GMAIL)
        val typeMatcher = hasType(ShareExMimeTypeEnum.ALL.textType)

        intended(allOf(actionMatcher, extraMatcher, packageMatcher, typeMatcher))
    }

    fun assertOthers() {
        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(Intent.EXTRA_TITLE, "Bagikan ke teman kamu"),
                hasExtra(
                    equalTo(Intent.EXTRA_INTENT),
                    allOf(
                        hasAction(Intent.ACTION_SEND),
                        hasType(ShareExMimeTypeEnum.TEXT.textType)
                    )
                )
            )
        )
    }
}
