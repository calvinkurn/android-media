#include <jni.h>
#include "bcaLibs/BCAdllE.h"
#include <string.h>
#include <malloc.h>
JNIEnv *g_env;
jobject g_obj;


/*
 * This function will convert from bcd to ascii
 */
void BcdToAscw(unsigned char *strBcd, int lenBcd, unsigned char *strAscii) {
    int i;
    int tmp;
    for (i = 0; i < lenBcd; i++) {
        tmp = (strBcd[i] & 0xF0) >> 4;
        if (tmp < 10) {
            strAscii[2 * i] = tmp + 48;
        } else strAscii[2 * i] = tmp + 55;
        tmp = (strBcd[i] & 0x0F);
        if (tmp < 10) {
            strAscii[2 * i + 1] = tmp + 48;
        } else strAscii[2 * i + 1] = tmp + 55;
    }
}

void AscToBcdw(unsigned char *strAscii, int lenAscii, unsigned char *strBcd) {

    int i;
    unsigned char Tmp;
    for (i = 0; i < lenAscii; i++) {

        if ((strAscii[i] >= '0') && (strAscii[i] <= '9'))              // numeric
            Tmp = strAscii[i] - '0';
        else if ((strAscii[i] >= 'A') && (strAscii[i] <= 'F')) // upper alpha
            Tmp = strAscii[i] - 'A' + 10;
        else if ((strAscii[i] >= 'a') && (strAscii[i] <= 'f')) // lower alpha
            Tmp = strAscii[i] - 'a' + 10;
        else if ((strAscii[i] >= 0x3a) && (strAscii[i] <= 0x3f)) // special char, such as '='
            Tmp = strAscii[i] - '0';
        else                             // others
            Tmp = 0;
        if (i % 2 == 0)
            strBcd[i / 2] = (Tmp << 4) & 0xf0;
        else
            strBcd[i / 2] |= Tmp & 0x0f;

    }

}

unsigned char BCAsendAPDU(unsigned char* cmdByte, unsigned short lenC,
                          unsigned char** RAPDU, unsigned short* lenR){
    //get JNIEnv and jobject from global var
    JNIEnv *env = g_env;
    jobject thisObj = g_obj;

    //set variable
    jint jLenC = lenC;
    unsigned char cmdString[lenC * 2 + 1];
    memset(cmdString, 0x00, sizeof(cmdString));
    BcdToAscw(cmdByte, lenC, cmdString); //convert byte into string
    jstring jcmdString = (*env)->NewStringUTF(env, cmdString);

    //call method sendContactlessAPDU(string cmdByte,int lenC) from java
    jclass clazz = (*env)->GetObjectClass(env, thisObj);
    jmethodID methodId = (*env)->GetMethodID(env, clazz, "sendContactlessAPDU",
                                             "(Ljava/lang/String;I)Ljava/lang/String;");
    jstring result = (*env)->CallObjectMethod(env, thisObj, methodId, jcmdString, jLenC);

    const char *rapduCStr;
    rapduCStr = (*env)->GetStringUTFChars(env, result, NULL);
    unsigned short lenRAPDUStr = ((*env)->GetStringLength(env, (jstring) result));

    if (lenR == 0) {
        return FALSE;
    }

    unsigned char rapduStr[lenRAPDUStr];
    memset(rapduStr, 0x00, sizeof(rapduStr));
    memcpy(rapduStr, rapduCStr, lenRAPDUStr);

    (*lenR) = lenRAPDUStr / 2;
    (*RAPDU) = malloc((*lenR) * sizeof(unsigned char));
    memset((*RAPDU), 0x00, (*lenR));
    AscToBcdw(rapduStr, lenRAPDUStr, (*RAPDU)); //convert string into byte

    (*env)->ReleaseStringUTFChars(env, result, rapduCStr);

    return TRUE;
}

JNIEXPORT jstring JNICALL
Java_com_tokopedia_emoney_integration_BCALibrary_C_1BCAVersionDll(JNIEnv *env, jobject thiz) {
    //create return value strLogRsp with length LENGTH_BCALIBVER with initial is 0 with size of LENGTH_BCALIBVER
    unsigned char strLogRsp[LENGTH_BCALIBVER];
    memset(strLogRsp, 0x00, sizeof(strLogRsp));

    //call library function BCAVersionDll
    unsigned char result = BCAVersionDll(strLogRsp);
    return (*env)->NewStringUTF(env, strLogRsp);
}

JNIEXPORT jstring JNICALL
Java_com_tokopedia_emoney_integration_BCALibrary_C_1BCAIsMyCard(JNIEnv *env, jobject thiz) {
    g_env = env;
    g_obj = thiz;
    //create return value strLogRsp with length LENGTH_BCAISMYCARD_RESULT with initial is 0 with size of LENGTH_BCAISMYCARD_RESULT
    unsigned char strLogRsp[LENGTH_BCAISMYCARD_RESULT];
    memset(strLogRsp, 0x00, sizeof(strLogRsp));

    //call library function BCAIsMyCard
    unsigned char result = BCAIsMyCard(strLogRsp);
    return (*env)->NewStringUTF(env, strLogRsp);
}

JNIEXPORT jobject JNICALL
Java_com_tokopedia_emoney_integration_BCALibrary_C_1BCACheckBalance(JNIEnv *env, jobject thiz) {
    g_env = env;
    g_obj = thiz;

    //create the object result class that will returned in jobject
    jclass resultClass = (*env)->FindClass(env, "com/tokopedia/emoney/integration/data/CheckBalanceResult");
    jmethodID constructor = (*env)->GetMethodID(env, resultClass, "<init>", "(ILjava/lang/String;)V");

    //create return value balance
    long balance = -1;
    //create return value cardNo with length LENGTH_STR_CARDNO with initial is 0 with size of LENGTH_STR_CARDNO
    unsigned char cardNo[LENGTH_STR_CARDNO];
    memset(cardNo, 0x00, sizeof(cardNo));
    //create return value strLogRsp with length LENGTH_BCASES_RESULT with initial is 0 with size of LENGTH_BCASES_RESULT
    unsigned char strLogRsp[LENGTH_BCASES_RESULT];
    memset(strLogRsp, 0x00, sizeof(strLogRsp));

    unsigned char result = BCACheckBalance(&balance, cardNo, strLogRsp);
    if (result == FALSE){
        //set the return value result fail
        jobject resultObject = (*env)->NewObject(env, resultClass, constructor, -1, (*env)->NewStringUTF(env, cardNo));
        return resultObject;
    }

    //set the return value result success
    jobject resultObject = (*env)->NewObject(env, resultClass, constructor, balance, (*env)->NewStringUTF(env, cardNo));
    return resultObject;
}