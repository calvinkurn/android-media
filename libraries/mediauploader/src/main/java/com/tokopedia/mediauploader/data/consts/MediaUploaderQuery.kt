package com.tokopedia.mediauploader.data.consts

object MediaUploaderQuery {

    private const val paramSourceId = "\$source"

    var dataPolicyQuery = """
        query dataPolicyQuery($paramSourceId: String){
          uploadpedia_policy(source: $paramSourceId){
            source_policy{
              host
              source_type
              image_policy{
                max_file_size
                max_res {
                  w
                  h
                }
                min_res {
                  w
                  h
                }
                allowed_ext
              }
            }
          }
        }
    """.trimIndent()

}