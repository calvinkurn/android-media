package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.autocompletecomponent.util.AutoCompleteUnifyConstant

data class SuggestionUnifyTitle(
    @SerializedName("icon_image_url")
    @Expose
    val iconImageUrl: String = "",

    @SerializedName("text")
    @Expose
    val text: String = "",

    @SerializedName("type")
    @Expose
    val type: String = ""
) {
    fun typeIsAdaptive(): Boolean {
        return type == AutoCompleteUnifyConstant.SUGGESTION_UNIFY_TITLE_TYPE_ADAPTIVE
    }
    fun typeIsBold(): Boolean {
        return type == AutoCompleteUnifyConstant.SUGGESTION_UNIFY_TITLE_TYPE_BOLD
    }
}
