package com.tokopedia.settingbank.domain

const val GQL_ADD_BANK_ACCOUNT = """mutation richieAddBankAccountNewFlow(${'$'}bankID: Int!,
                                    ${'$'}bankName: String!, 
                                    ${'$'}accountNo: String!,
                                    ${'$'}accountName:String!,
                                    ${'$'}isManual : Boolean!){
                                    richieAddBankAccountNewFlow
                                          (   bankID:${'$'}bankID,
                                                bankName:${'$'}bankName,
                                                accountNo:${'$'}accountNo,
                                                accountName:${'$'}accountName,
                                                isManual:${'$'}isManual
                                            ){
                                       status
                                      message
                                      data{
                                              accID
                                              accNo
                                              accName
                                              bankID
                                              userID
                                        }
                                      }
                                    }"""

const val GQL_GET_TERMS_CONDITION = """query RichieGetTNCBankAccount(${'$'}type : String!){
                                        RichieGetTNCBankAccount(type : ${'$'}type){
                                            status
                                            message
                                            data{
                                              template
                                            }
                                          }
                                        }"""

const val GQL_CHECK_ACCOUNT_NUMBER ="""query CheckAccountNumber(${'$'}bankId : Int!,
                                        ${'$'}accountNumber:String!,
                                        ${'$'}editedAccountName:String,
                                        ${'$'}action:String){
                                        CheckAccountNumber(bankID:${'$'}bankId,
                                            accountNumber:${'$'}accountNumber,
                                            editedAccountName:${'$'}editedAccountName,
                                            action:${'$'}action){
                                               successCode,
                                               message,
                                               bankID,
                                               accountNumber,
                                               bankName,
                                               accountName,
                                               isValidBankAccount,
                                               allowedToEdit
                                         }
                                    }"""

const val GQL_DELETE_BANK_ACCOUNT = """mutation (${'$'}accID: Int!){
                                          DeleteBankAccount(accID:${'$'}accID){
                                            status
                                            header{
                                              message
                                              processTime
                                              reason
                                              errorCode
                                            }
                                            data{
                                              success
                                              message
                                            }
                                          }
                                        }"""

const val GQL_GET_BANK_LIST = """query RichieGetBankList(${'$'}page:Int, ${'$'}perPage:Int){
                                  RichieGetBankList(page:${'$'}page, perPage:${'$'}perPage){
                                    status
                                    header{
                                      processTime
                                      messages
                                      reason
                                      errorCode
                                    }
                                    data {
                                      banks{
                                        bankID
                                        bankName
                                        clearingCode
                                        abbreviation
                                      }
                                    }
                                  }
                                }"""

const val GQL_GET_USER_BANK_ACCOUNT_LIST = """query {
                                              GetBankAccount(){
                                                status
                                                header{
                                                  processTime
                                                  message
                                                  reason
                                                  errorCode
                                                }
                                                data{
                                                  bankAccounts{
                                                    accID
                                                    accName
                                                    accNumber
                                                    bankID
                                                    bankName
                                                    statusFraud
                                                    bankImageUrl
                                                    copyWriting
                                                  }
                                                  userInfo{
                                                    message
                                                    isVerified
                                                  }
                                                }
                                              }
                                            }"""

const val GQL_BANK_KYC_CHECK = """query {
                                    kycInfo() {
                                        Fullname
                                        IsVerified
                                        IsExist
                                    }
                                }"""