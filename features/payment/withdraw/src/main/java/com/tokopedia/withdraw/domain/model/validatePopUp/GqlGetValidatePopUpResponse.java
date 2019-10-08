package com.tokopedia.withdraw.domain.model.validatePopUp;

import com.google.gson.annotations.SerializedName;

public class GqlGetValidatePopUpResponse{

	@SerializedName("ValidatePopUpWithdrawal")
	private ValidatePopUpWithdrawal validatePopUpWithdrawal;

	public void setValidatePopUpWithdrawal(ValidatePopUpWithdrawal validatePopUpWithdrawal){
		this.validatePopUpWithdrawal = validatePopUpWithdrawal;
	}

	public ValidatePopUpWithdrawal getValidatePopUpWithdrawal(){
		return validatePopUpWithdrawal;
	}
}