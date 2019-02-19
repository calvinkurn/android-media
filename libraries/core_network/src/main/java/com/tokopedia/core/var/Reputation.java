package com.tokopedia.core.var;

@Deprecated
public class Reputation{
	private int badgeLevel;
	private String badge;
	private int score;
	private String reputationScore;
	private String tooltip;
	private int minBadgeScore;

	public void setBadgeLevel(int badgeLevel){
		this.badgeLevel = badgeLevel;
	}

	public int getBadgeLevel(){
		return badgeLevel;
	}

	public void setBadge(String badge){
		this.badge = badge;
	}

	public String getBadge(){
		return badge;
	}

	public void setScore(int score){
		this.score = score;
	}

	public int getScore(){
		return score;
	}

	public void setReputationScore(String reputationScore){
		this.reputationScore = reputationScore;
	}

	public String getReputationScore(){
		return reputationScore;
	}

	public void setTooltip(String tooltip){
		this.tooltip = tooltip;
	}

	public String getTooltip(){
		return tooltip;
	}

	public void setMinBadgeScore(int minBadgeScore){
		this.minBadgeScore = minBadgeScore;
	}

	public int getMinBadgeScore(){
		return minBadgeScore;
	}

	@Override
 	public String toString(){
		return 
			"Reputation{" + 
			"badge_level = '" + badgeLevel + '\'' + 
			",badge = '" + badge + '\'' + 
			",score = '" + score + '\'' + 
			",reputation_score = '" + reputationScore + '\'' + 
			",tooltip = '" + tooltip + '\'' + 
			",min_badge_score = '" + minBadgeScore + '\'' + 
			"}";
		}
}
