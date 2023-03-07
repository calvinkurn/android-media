package com.tokopedia.home_account.explicitprofile.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ExplicitprofileGetQuestion(
    @SerializedName("explicitprofileGetQuestion")
    var explicitProfileQuestionDataModel: ExplicitProfileGetQuestionDataModel = ExplicitProfileGetQuestionDataModel()
)

data class ExplicitProfileGetQuestionDataModel(
    @SerializedName("activeConfig")
    var activeConfigData: ActiveConfigDataModel = ActiveConfigDataModel(),
    @SerializedName("template")
    var template: TemplateDataModel = TemplateDataModel()
)

data class ActiveConfigDataModel(
    @SerializedName("value")
    var value: Boolean = false,
    @SerializedName("detail")
    var detail: Detail = Detail()
) {
    data class Detail(
        @SerializedName("configActive")
        var configActive: Boolean = false,
        @SerializedName("templateActive")
        var templateActive: Boolean = false
    )
}

data class TemplateDataModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("property")
    var property: Property = Property(),
    @SerializedName("sections")
    var sections: MutableList<SectionsDataModel> = mutableListOf()
) {
    data class Property(
        @SerializedName("title")
        var title: String = "",
        @SerializedName("image")
        var image: String = ""
    )
}

@Parcelize
data class SectionsDataModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("sectionID")
    var sectionId: Int = 0,
    @SerializedName("maxAnswer")
    var maxAnswer: Int = 0,
    @SerializedName("layout")
    var layout: String = "",
    @SerializedName("property")
    var property: Property = Property(),
    @SerializedName("questions")
    var questions: MutableList<QuestionDataModel> = mutableListOf()
) : Parcelable {

    @Parcelize
    data class Property(
        @SerializedName("title")
        var title: String = "",
        @SerializedName("infoHeader")
        var infoHeader: String = ""
    ) : Parcelable
}

@Parcelize
data class QuestionDataModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("questionId")
    var questionId: Int = 0,
    @SerializedName("questionType")
    var questionType: String = "",
    @SerializedName("property")
    var property: Property = Property(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("answerId")
    var answerId: Int = 0,
    @SerializedName("answerValue")
    var answerValue: String = ""
) : Parcelable {

    @Parcelize
    data class Property(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("image")
        var image: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("subtitle")
        var subTitle: String = "",
        @SerializedName("infoContent")
        var infoContent: String = "",
        @SerializedName("options")
        var options: MutableList<Options> = mutableListOf()
    ) : Parcelable {

        @Parcelize
        data class Options(
            @SerializedName("value")
            var value: String = "",
            @SerializedName("caption")
            var caption: String = "",
            @SerializedName("message")
            var message: String = ""
        ): Parcelable
    }
}
