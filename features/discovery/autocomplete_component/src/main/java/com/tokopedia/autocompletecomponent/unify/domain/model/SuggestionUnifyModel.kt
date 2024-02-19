package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class UnifyModelContract {
    abstract val data: UniverseSuggestionUnifyModel
}

data class InitialStateUnifyModel(
    @SerializedName("universe_initial_state_unify")
    @Expose
    override val data: UniverseSuggestionUnifyModel = UniverseSuggestionUnifyModel()
) : UnifyModelContract()

data class SuggestionUnifyModel(
    @SerializedName("universe_suggestion_unify")
    @Expose
    override val data: UniverseSuggestionUnifyModel = UniverseSuggestionUnifyModel()
) : UnifyModelContract()
