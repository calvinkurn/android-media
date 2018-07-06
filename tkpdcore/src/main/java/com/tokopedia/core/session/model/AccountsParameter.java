package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stevenfredian on 6/16/16.
 */
public class AccountsParameter implements Parcelable {


    private String email;
    private String password;
    private int loginType;
    private String grantType;
    private int socialType;
    private String code;
    private String redirectUri;
    private TokenModel tokenModel;
    private Parcelable parcelable;
    private InfoModel infoModel;
    private String UUID;
    private SecurityModel securityModel;
    private boolean moveSecurity;
    private boolean activationResent;
    private AccountsModel accountsModel;
    private ErrorModel errorModel;
    private String attempt;
    private String passwordType;
    private String scope;

    public ErrorModel getErrorModel() {
        return errorModel;
    }

    public void setErrorModel(ErrorModel errorModel) {
        this.errorModel = errorModel;
    }

    public InfoModel getInfoModel() {
        return infoModel;
    }

    public void setInfoModel(InfoModel infoModel) {
        this.infoModel = infoModel;
    }


    public AccountsModel getAccountsModel() {
        return accountsModel;
    }

    public void setAccountsModel(AccountsModel accountsModel) {
        this.accountsModel = accountsModel;
    }

    public SecurityModel getSecurityModel() {
        return securityModel;
    }

    public void setSecurityModel(SecurityModel securityModel) {
        this.securityModel = securityModel;
    }

    public boolean isMoveSecurity() {
        return moveSecurity;
    }

    public void setMoveSecurity(boolean moveSecurity) {
        this.moveSecurity = moveSecurity;
    }

    public boolean isActivationResent() {
        return activationResent;
    }

    public void setActivationResent(boolean activationResent) {
        this.activationResent = activationResent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getSocialType() {
        return socialType;
    }

    public void setSocialType(int socialType) {
        this.socialType = socialType;
    }

    public Parcelable getParcelable() {
        return parcelable;
    }

    public void setParcelable(Parcelable parcelable) {
        this.parcelable = parcelable;
    }

    public TokenModel getTokenModel() {
        return tokenModel;
    }

    public void setTokenModel(TokenModel tokenModel) {
        this.tokenModel = tokenModel;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(String passwordType) {
        this.passwordType = passwordType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public AccountsParameter() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeInt(this.loginType);
        dest.writeString(this.grantType);
        dest.writeInt(this.socialType);
        dest.writeString(this.code);
        dest.writeString(this.redirectUri);
        dest.writeParcelable(this.tokenModel, flags);
        dest.writeParcelable(this.parcelable, flags);
        dest.writeParcelable(this.infoModel, flags);
        dest.writeString(this.UUID);
        dest.writeParcelable(this.securityModel, flags);
        dest.writeByte(this.moveSecurity ? (byte) 1 : (byte) 0);
        dest.writeByte(this.activationResent ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.accountsModel, flags);
        dest.writeParcelable(this.errorModel, flags);
        dest.writeString(this.attempt);
        dest.writeString(this.passwordType);
        dest.writeString(this.scope);
    }

    protected AccountsParameter(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.loginType = in.readInt();
        this.grantType = in.readString();
        this.socialType = in.readInt();
        this.code = in.readString();
        this.redirectUri = in.readString();
        this.tokenModel = in.readParcelable(TokenModel.class.getClassLoader());
        this.parcelable = in.readParcelable(Parcelable.class.getClassLoader());
        this.infoModel = in.readParcelable(InfoModel.class.getClassLoader());
        this.UUID = in.readString();
        this.securityModel = in.readParcelable(SecurityModel.class.getClassLoader());
        this.moveSecurity = in.readByte() != 0;
        this.activationResent = in.readByte() != 0;
        this.accountsModel = in.readParcelable(AccountsModel.class.getClassLoader());
        this.errorModel = in.readParcelable(ErrorModel.class.getClassLoader());
        this.attempt = in.readString();
        this.passwordType = in.readString();
        this.scope = in.readString();
    }

    public static final Creator<AccountsParameter> CREATOR = new Creator<AccountsParameter>() {
        @Override
        public AccountsParameter createFromParcel(Parcel source) {
            return new AccountsParameter(source);
        }

        @Override
        public AccountsParameter[] newArray(int size) {
            return new AccountsParameter[size];
        }
    };
}
