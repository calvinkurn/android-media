#ifndef BCADLLT_H_
#define BCADLLT_H_

#ifdef __cplusplus
extern "C" {
#endif


#define TRUE							1
#define FALSE							0


#define LENGTH_ACCESS_TOKEN				45		// Access Token Data
#define LENGTH_REQ_DATA					448		// Length of REQUEST Data		
#define LENGTH_RSP_DATA					350		// Length of RESPONSE Data

#define LENGTH_SES_DATA					198		// Length of Session Data

#define LENGTH_TUP_DATA					544		// Length of ACK Data			
#define LENGTH_RES_CODE					4		// Length of Response Code

#define LENGTH_RESPONSE					LENGTH_RES_CODE + 1						// Length of Response Code of Function
#define LENGTH_BCALIBVER				16										// Length of LIB Version Information
#define LENGTH_BCATUP_1					LENGTH_RES_CODE + LENGTH_REQ_DATA + 1	// Length of BCAToUp_1's Result
#define LENGTH_BCATUP_2					LENGTH_RES_CODE + LENGTH_TUP_DATA + 1	// Length of BCAToUp_2's Result
#define LENGTH_BCAREV_RESULT			LENGTH_RES_CODE + LENGTH_TUP_DATA + 1	// Length of BCAdataReversal's Result
#define LENGTH_BCACRDINFO_RESULT		LENGTH_RES_CODE + LENGTH_TUP_DATA + 1	// Length of BCAdataCardInfo's Result
#define LENGTH_BCASES_RESULT			LENGTH_RES_CODE + LENGTH_SES_DATA + 1	// Length of BCAdataSession's Result
#define LENGTH_BCAISMYCARD_RESULT		LENGTH_RES_CODE + 17					// Length of BCAIsMyCard's Result		

#define LENGTH_STR_CONFIG				25		// Length of 'strConfig'
#define LENGTH_STR_CARDNO				17		// Length of 'CardNo'
#define LENGTH_STR_ACC_CODE				7		// Length of 'Access CODE'
#define LENGTH_STR_DATETIME				15		// Length of 'strCurrDateTime' // yyyyMMddHHmmss
#define LENGTH_STR_TRANSACTION_ID		17		// Length of 'strTransactionID' 	




/*
 * ********************************** VERSION BCA DLL ******************************************
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_BCALIBVER
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAVersionDll(unsigned char *strLogRsp);


/*
 * ********************************** SET BCA CONFIG *******************************************
 * 	Input:
 * 			strConfig		:	SET Configuration of DLL as string
 * 								Length: LENGTH_STR_CONFIG
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_RESPONSE
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCASetConfig(unsigned char *strConfig, unsigned char *strLogRsp);


/*
 * ********************************** GET BCA CONFIG *******************************************
 * 	Output:
 * 			strConfig		:	GET Configuration of DLL as string
 * 								Length: LENGTH_STR_CONFIG
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_RESPONSE
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAGetconfig(unsigned char *strConfig, unsigned char *strLogRsp);


/*
 * ***************************************** IS MY CARD ****************************************
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_BCAISMYCARD_RESULT
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAIsMyCard(unsigned char *strLogRsp);


/*
 * *************************************** CHECK BALANCE ***************************************
 * 	Output:
 * 			Balance  		: 	Balance of Flazz Card, size as Number LONG
 * 			CardNo			:	Card Number
								Length: LENGTH_STR_CARDNO
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_RESPONSE
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCACheckBalance(long *Balance, unsigned char *CardNo, unsigned char *strLogRsp);


/*
 * ************************************** SESSION 1 ********************************************
 * 	Input:
 *			strTransactionID:	Transaction ID
 *								Length: LENGTH_STR_TRANSACTION_ID
 * 			ATD				:	Access Token Data
 * 								Length: LENGTH_ACCESS_TOKEN
 * 			strCurrDateTime	:	Date Time of Current Transaction
 * 								Format: yyyyMMddHHmmss
 * 								Length: LENGTH_STR_DATETIME
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_BCASES_RESULT
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAdataSession_1( unsigned char *strTransactionID, unsigned char *ATD, unsigned char * strCurrDateTime, unsigned char *strLogRsp );


/*
 * ************************************** SESSION 2 ********************************************
 * 	Input:
 * 			ResponseData	:	Data from Host
 * 								Length: LENGTH_SES_DATA
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_RESPONSE
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAdataSession_2( unsigned char *ResponseData, unsigned char *strLogRsp );


/*
 * ***********************************  Top Up 1 ***********************************************
 * 	Input:
 *			strTransactionID	:	Transaction ID
 *									Length: LENGTH_STR_TRANSACTION_ID
 * 			ATD					:	Access Token Data
 * 									Length: LENGTH_ACCESS_TOKEN
 * 			strAccessCardNumber	:	Access Card Number
 * 									Length: LENGTH_STR_CARDNO
 * 			strAccessCode		:	Access Code
 * 									Length: LENGTH_STR_ACC_CODE
 * 			strCurrDateTime		:	Date Time of Current Transaction
 * 									Format: yyyyMMddHHmmss
 * 									Length: LENGTH_STR_DATETIME
 * 			lngAmount  			: 	Amount TopUp
 * 	Output:
 * 			strLogRsp			:	Log/Data Response
 * 									Length: LENGTH_BCATUP_1
 * 			return function		:	TRUE as Successful
 * 									FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCATopUp_1(unsigned char *strTransactionID, unsigned char *ATD, unsigned char *strAccessCardNumber, unsigned char *strAccessCode, unsigned char *strCurrDateTime, long lngAmount, unsigned char *strLogRsp);


/*
 * ***********************************  Top Up 2 ***********************************************
 * 	Input:
 * 			ResponseData	:	Data from Host
 * 								Length: LENGTH_RSP_DATA
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Length: LENGTH_BCATUP_2
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCATopUp_2(unsigned char *ResponseData, unsigned char *strLogRsp);

 
/*
 * ***********************************  Reversal ***********************************************
 * 	Input:
 *			strTransactionID	:	Transaction ID
 *									Length: LENGTH_STR_TRANSACTION_ID
 * 			ATD					:	Access Token Data
 * 									Length: LENGTH_ACCESS_TOKEN
 * 	Output:
 * 			strLogRsp			:	Log/Data Response
 * 									Length: LENGTH_BCAREV_RESULT
 * 			return function		:	TRUE as Successful
 * 									FALSE as Failed
 *
 * *********************************************************************************************
 */ 
unsigned char BCAdataReversal(unsigned char *strTransactionID, unsigned char *ATD, unsigned char *strLogRsp);


/*
 * ********************************* Last Top Up ***********************************************
 * 	Output:
 * 			strLogRsp			:	Log/Data Response
 * 									Length: LENGTH_BCATUP_2
 * 			return function		:	TRUE as Successful
 * 									FALSE as Failed
 *
 * *********************************************************************************************
 */ 
unsigned char BCAlastBCATopUp(unsigned char *strLogRsp);


/*
 * *********************************  Get Card Info ********************************************
 * 	Input:
 *			strTransactionID	:	Transaction ID
 *									Length: LENGTH_STR_TRANSACTION_ID
 * 			ATD					:	Access Token Data
 * 									Length: LENGTH_ACCESS_TOKEN
 * 	Output:
 * 			strLogRsp			:	Log/Data Response
 * 									Length: LENGTH_BCACRDINFO_RESULT
 * 			return function		:	TRUE as Successful
 * 									FALSE as Failed
 *
 * *********************************************************************************************
 */ 
unsigned char BCAdataCardInfo(unsigned char *strTransactionID, unsigned char *ATD, unsigned char *strLogRsp);




/*
 * ////////////////////////////////////////////////////// Defined in Third Party's Object \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 */
	
	
/*
 * *************************************** SENDAPDU ********************************************
 * Function to Send APDU's command to Contactless Reader
 *
 * Input:
 * 			cmdByte			:	Command APDU, BCD format
 * 			lenC			:	Length of Command APDU
 *
 * 	Output:
 * 			RAPDU			:	Response of Command APDU
 * 			lenR			:	Length of RAPDU
 *
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAsendAPDU(unsigned char* cmdByte, unsigned short lenC, unsigned char** RAPDU, unsigned short* lenR);


/*
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//////////////////////////////////////////////////////////////
 */

#ifdef __cplusplus
}
#endif
#endif /* BCADLLT_H_ */
