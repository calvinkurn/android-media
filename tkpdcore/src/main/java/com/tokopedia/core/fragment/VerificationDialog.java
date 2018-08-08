package com.tokopedia.core.fragment;

/**
 * @author Nanda Julianda Akbar
 * @since 10/06/2015
 * commit Id : 7fd70cd
 *
 * // other
 * modified by m.normansyah 23/11/2015
 *
 */
@Deprecated
public class VerificationDialog{
	public static final int PopUpVerificationMode = 0;
	public static final int VerificationFromProfileSettings = 1;
	public static final int RequestChangeNumberFromProfile = 2;
	public static final int remindVerifyNumber = 3;// This is invalid
	public static final int IncomingSmsWarning = 4;

	public interface VerificationUpdate{
		void onVerified(String phoneNumber);
	}


}
