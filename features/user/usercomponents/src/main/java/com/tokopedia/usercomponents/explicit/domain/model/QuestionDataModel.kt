package com.tokopedia.usercomponents.explicit.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class OptionsItem(
    @SerializedName("caption")
    val caption: String = "",

    @SerializedName("value")
    val value: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("applink")
    val applink: String = "",

    @SerializedName("textApplink")
    val textApplink: String = ""
)

data class Property(
    @SerializedName("image")
    val image: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("subtitle")
    val subtitle: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("options")
    val options: List<OptionsItem> = mutableListOf(OptionsItem(), OptionsItem())
)

data class QuestionDataModel(
    @SerializedName("explicitprofileGetQuestion")
    val explicitprofileGetQuestion: ExplicitprofileGetQuestion = ExplicitprofileGetQuestion()
)

data class ExplicitprofileGetQuestion(

    @SerializedName("activeConfig")
    val activeConfig: ActiveConfig = ActiveConfig(),

    @SerializedName("template")
    val template: Template = Template()
)

data class ActiveConfig(
    @SerializedName("value")
    val value: Boolean = false
)

data class SectionsItem(
    @SerializedName("questions")
    val questions: List<QuestionsItem> = mutableListOf(QuestionsItem()),

    @SuppressLint("Invalid Data Type") @SerializedName("sectionID")
    val sectionID: Int = 0
)

data class QuestionsItem(
    @SerializedName("answerId")
    val answerId: String = "",

    @SuppressLint("Invalid Data Type") @SerializedName("questionId")
    val questionId: Int = 0,

    @SerializedName("answerValue")
    val answerValue: String = "",

    @SerializedName("property")
    val property: Property = Property()
)

data class Template(
    @SerializedName("name")
    val name: String = "",

    @SuppressLint("Invalid Data Type") @SerializedName("id")
    val id: Int = 0,

    @SerializedName("sections")
    val sections: List<SectionsItem> = mutableListOf(SectionsItem())
)
