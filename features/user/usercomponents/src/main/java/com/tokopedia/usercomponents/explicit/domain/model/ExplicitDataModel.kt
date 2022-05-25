package com.tokopedia.usercomponents.explicit.domain.model

import com.google.gson.annotations.SerializedName

data class OptionsItem(

	@field:SerializedName("caption")
	val caption: String = "",

	@field:SerializedName("message")
	val message: String = "",

	@field:SerializedName("value")
	val value: String = ""
)

data class Property(

	@field:SerializedName("image")
	val image: String = "",

	@field:SerializedName("title")
	val title: String = "",

	@field:SerializedName("infoHeader")
	val infoHeader: String = "",

	@field:SerializedName("subtitle")
	val subtitle: String = "",

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("options")
	val options: List<OptionsItem> = emptyList(),

	@field:SerializedName("infoContent")
	val infoContent: String = ""
)

data class Detail(

	@field:SerializedName("configActive")
	val configActive: Boolean = false,

	@field:SerializedName("templateActive")
	val templateActive: Boolean = false
)

data class ExplicitDataModel(
	@field:SerializedName("explicitprofileGetQuestion")
	val explicitprofileGetQuestion: ExplicitprofileGetQuestion = ExplicitprofileGetQuestion()
)

data class ActiveConfig(

	@field:SerializedName("detail")
	val detail: Detail = Detail(),

	@field:SerializedName("value")
	val value: Boolean = false
)

data class ExplicitprofileGetQuestion(

	@field:SerializedName("template")
	val template: Template = Template(),

	@field:SerializedName("activeConfig")
	val activeConfig: ActiveConfig = ActiveConfig()
)

data class SectionsItem(

	@field:SerializedName("layout")
	val layout: String = "",

	@field:SerializedName("property")
	val property: Property = Property(),

	@field:SerializedName("questions")
	val questions: List<QuestionsItem> = emptyList(),

	@field:SerializedName("sectionID")
	val sectionID: String = ""
)

data class QuestionsItem(

	@field:SerializedName("answerId")
	val answerId: String = "",

	@field:SerializedName("questionId")
	val questionId: String = "",

	@field:SerializedName("answerValue")
	val answerValue: String = "",

	@field:SerializedName("property")
	val property: Property = Property(),

	@field:SerializedName("questionType")
	val questionType: String = ""
)

data class Template(

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("property")
	val property: Property = Property(),

	@field:SerializedName("description")
	val description: String = "",

	@field:SerializedName("id")
	val id: String = "",

	@field:SerializedName("sections")
	val sections: List<SectionsItem> = emptyList()
)
