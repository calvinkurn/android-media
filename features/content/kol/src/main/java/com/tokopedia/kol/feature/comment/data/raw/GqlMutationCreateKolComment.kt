package com.tokopedia.kol.feature.comment.data.raw

const val GQL_MUTATION_CREATE_KOL_COMMENT: String = """mutation CreateKolComment(${'$'}idPost: Int!, ${'$'}comment: String!) {
 create_comment_kol(idPost: ${'$'}idPost, comment: ${'$'}comment) {	
 __typename 
 error
 data {
   __typename
   id
   user {
     __typename
     iskol
     id
     name
     photo
   }
   comment
   create_time
 }
}
}"""