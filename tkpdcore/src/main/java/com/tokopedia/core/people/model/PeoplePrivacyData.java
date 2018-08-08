package com.tokopedia.core.people.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 5/31/16.
 */
public class PeoplePrivacyData {

    @SerializedName("privacy")
    private Privacy privacy;

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public static class Privacy {
        @SerializedName("flag_hp")
        private String flagHp;
        @SerializedName("flag_birthdate")
        private String flagBirthdate;
        @SerializedName("flag_email")
        private String flagEmail;
        @SerializedName("flag_address")
        private String flagAddress;
        @SerializedName("flag_messenger")
        private String flagMessenger;

        public String getFlagHp() {
            return flagHp;
        }

        public void setFlagHp(String flagHp) {
            this.flagHp = flagHp;
        }

        public String getFlagBirthdate() {
            return flagBirthdate;
        }

        public void setFlagBirthdate(String flagBirthdate) {
            this.flagBirthdate = flagBirthdate;
        }

        public String getFlagEmail() {
            return flagEmail;
        }

        public void setFlagEmail(String flagEmail) {
            this.flagEmail = flagEmail;
        }

        public String getFlagAddress() {
            return flagAddress;
        }

        public void setFlagAddress(String flagAddress) {
            this.flagAddress = flagAddress;
        }

        public String getFlagMessenger() {
            return flagMessenger;
        }

        public void setFlagMessenger(String flagMessenger) {
            this.flagMessenger = flagMessenger;
        }
    }
}
