package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query


internal const val ADMIN_CONFIRMATION_REG_QUERY = """
        mutation adminConfirmationReg(${'$'}source: String!, ${'$'}shopID: String!, ${'$'}userId: String!, ${'$'}shopManageId: String!, ${'$'}acceptBecomeAdmin: Boolean!) {
          adminConfirmationReg(input: {
            source: ${'$'}source,
            shopID: ${'$'}shopID,
            userId: ${'$'}userId,
            acceptBecomeAdmin: ${'$'}acceptBecomeAdmin,
            shopManageId: ${'$'}shopManageId
          }) {
              success
              message
              acceptBecomeAdmin
            }
        }   
    """

internal const val GET_SHOP_ADMIN_INFO_QUERY = """
        query getShopAdminInfo(${'$'}source: String!, ${'$'}shop_id: Int!, ${'$'}input: ParamShopInfoByID!) {
            shopInfoByID(input: ${'$'}input) {
                result {
                  shopCore {
                    name
                  }
                  shopAssets {
                    avatar
                  }
                }
           }
           getAdminInfo(source: ${'$'}source, shop_id: ${'$'}shop_id) {
              admin_data {
                 shop_manage_id
              }
           }
        }
    """

internal const val USER_PROFILE_UPDATE_QUERY = """
        mutation userProfileUpdate(${'$'}email: String!) {
          userProfileUpdate(email: ${'$'}email) {
              isSuccess
              errors
            }
        }   
    """

internal const val VALIDATE_ADMIN_EMAIL_QUERY = """
        mutation validateAdminEmail(${'$'}email: String!, ${'$'}manageID: String!, ${'$'}shopID: String!) {
            validateAdminEmail(input: {
                source : "validate-admin-email-android",
                email : ${'$'}email,
                manageID: ${'$'}manageID,
                shopID: ${'$'}shopID
              }) {
                  success
                  message
                  data {
                    newUser
                    existsUser
                    userName
                  }
              }
        }
    """