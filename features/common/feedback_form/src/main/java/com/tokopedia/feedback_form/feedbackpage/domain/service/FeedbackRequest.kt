package com.tokopedia.feedback_form.feedbackpage.domain.service

data class FeedbackRequest(
        var fields: Fields
)

data class Fields(
        var summary : String,
        var project : Project,
        var issuetype : Issuetype,
        var description : Description,
        var customfield_10077 : Customfield_10077,
        var customfield_10548 : List<Customfield_10548>,
        var customfield_10253 : Customfield_10253,
        var customfield_10181 : Customfield_10181,
        var labels : List<String>,
        var customfield_10550 : List<String>,
        var versions : List<Version>
)

data class Project(
        var id : String,
        var key : String,
        var name : String
)

data class Issuetype(
        var id : String,
        var name : String
)

data class Description(
        var version : Int,
        var type : String,
        var content : List<Content>
)

data class Reporter(
        var id: String
)

data class FixVersion(
        var id: String
)

data class Content(
        var type : String,
        var content : List<DeepContent>
)

data class DeepContent(
        var type : String,
        var text: String
)

data class Customfield_10077(
        var value : String,
        var id : String
)

data class Customfield_10548(
        var value : String,
        var id : String
)

data class Customfield_10253(
        var value : String,
        var id : String
)

data class Customfield_10181(
        var value : String,
        var id : String
)

data class Version(
        var name : String
)