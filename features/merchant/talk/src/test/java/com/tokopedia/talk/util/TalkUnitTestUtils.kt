package com.tokopedia.talk.util

import androidx.lifecycle.LiveData
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormCategory
import junit.framework.Assert.assertTrue

val unselectedCategories: List<DiscussionGetWritingFormCategory> = listOf(
        DiscussionGetWritingFormCategory("Stock", ""),
        DiscussionGetWritingFormCategory("Varian", ""),
        DiscussionGetWritingFormCategory("Deskripsi Produk", ""),
        DiscussionGetWritingFormCategory("Logistik", ""),
        DiscussionGetWritingFormCategory("Lainnya", "")
)

internal fun <T: TalkTemplateMutationResults> LiveData<T>.verifyTemplateMutationErrorEquals(talkTemplateMutationResults: TalkTemplateMutationResults) {
    when (value) {
        is TalkTemplateMutationResults.MutationFailed -> {
            assertTrue(talkTemplateMutationResults is TalkTemplateMutationResults.MutationFailed)
        }
        is TalkTemplateMutationResults.RearrangeTemplateFailed -> {
            assertTrue(talkTemplateMutationResults is TalkTemplateMutationResults.RearrangeTemplateFailed)
        }
        is TalkTemplateMutationResults.DeleteTemplateFailed -> {
            assertTrue(talkTemplateMutationResults is TalkTemplateMutationResults.DeleteTemplateFailed)
        }
    }
}