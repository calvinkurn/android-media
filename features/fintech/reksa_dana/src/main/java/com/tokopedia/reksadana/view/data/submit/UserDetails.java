package com.tokopedia.reksadana.view.data.submit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails {
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("education")
    private String education;
    @Expose
    @SerializedName("occupation")
    private String occupation;
    @Expose
    @SerializedName("income")
    private String income;
    @Expose
    @SerializedName("incomeSource")
    private String incomeSource;
    @Expose
    @SerializedName("investmentObjective")
    private String investmentObjective;
    @Expose
    @SerializedName("identityFileURL")
    private String identityFileURL;
    @Expose
    @SerializedName("signatureFileBase64")
    private String signatureFileBase64;

    public UserDetails(String email, String education, String occupation, String income,
                       String incomeSource, String investmentObjective, String identityFileURL,
                       String signatureFileBase64) {
        this.email = email;
        this.education = education;
        this.occupation = occupation;
        this.income = income;
        this.incomeSource = incomeSource;
        this.investmentObjective = investmentObjective;
        this.identityFileURL = identityFileURL;
        this.signatureFileBase64 = signatureFileBase64;
    }

    public String email() {
        return email;
    }

    public String education() {
        return education;
    }

    public String occupation() {
        return occupation;
    }

    public String income() {
        return income;
    }

    public String incomeSource() {
        return incomeSource;
    }

    public String investmentObjective() {
        return investmentObjective;
    }

    public String identityFileURL() {
        return identityFileURL;
    }

    public String signatureFileBase64() {
        return signatureFileBase64;
    }

    @Override
    public String toString() {
        return "UserDetails:["+
                "email="+email+", "+
                "education="+education+", "+
                "occupation="+occupation+", "+
                "income="+income+", "+
                "incomeSource="+incomeSource+", "+
                "investmentObjective="+investmentObjective+", "+
                "identityFileURL="+identityFileURL+", "+"]";

    }
}
