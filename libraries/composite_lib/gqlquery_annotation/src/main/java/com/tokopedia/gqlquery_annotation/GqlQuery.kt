@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GqlQuery(
        val queryName: String,
        val queryValue: String
)