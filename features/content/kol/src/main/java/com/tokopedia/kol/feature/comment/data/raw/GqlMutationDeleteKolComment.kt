package com.tokopedia.kol.feature.comment.data.raw

const val GQL_MUTATION_DELETE_KOL_COMMENT: String = """mutation DeleteKolComment(${'$'}idComment: Int!) {
	delete_comment_kol(idComment: ${'$'}idComment) {
		error
		data {
			success
			}
	}
}"""