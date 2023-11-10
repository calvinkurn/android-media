---
title: "Shop Settings"
labels:
- shop-showcase
- feature
- shop-operational-hour
- shop-name
- seller
- android
- shop-domain
- shop
- shop-etalase
---


| **Status** |  <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence)  |
| Product Manager | [Monica Marianes](https://tokopedia.atlassian.net/wiki/people/613e9e4fe057c6006a0fd233?ref=confluence) [Joshua Ghibran](https://tokopedia.atlassian.net/wiki/people/70121:7d12fd85-be0a-4d0c-a14e-8279fe20ff69?ref=confluence)  |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178?ref=directory&src=peopleMenu)  |
| Module type | <!--start status:RED-->FEATURE<!--end status--> |
| Module Location | `features/merchant/shop_settings` |

## Table of Contents

<!--toc-->

## Overview

![](res/Screen%20Shot%202023-05-09%20at%2009.17.16.png)

Shop Settings module consists of several packages:

1. analytics: Contains tracking utils for shop settings module
2. basicinfo:


	1. Description: Contains several settings page. You can use this feature to configure your basic information for your shop
	2. Gql: 
	
	
		1. `allowShopNameDomainChanges`[Validate Shop Name & Domain](/wiki/spaces/MC/pages/588972631)
		2. `setShopOperationalHours` [Shop Operational Hours - GQL Mutation](/wiki/spaces/MC/pages/742199205/Shop+Operational+Hours+-+GQL+Mutation)
	3. Features:
	
	
		1. ShopEditScheduleActivity 
		
		
			1. Description: You can use this feature to open/ close your shop
			2. Applink: `tokopedia-android-internal://marketplace/shop-settings-edit-schedule` 
			
			![](res/Screen%20Shot%202023-05-17%20at%2014.47.23.png)
		2. ShopSettingsInfoActivity
		
		
			1. Description: You can use this feature to open/ close your shop
			2. Applink: `tokopedia-android-internal://marketplace/shop-settings-info` 
			
			![](res/Screen%20Shot%202023-05-17%20at%2014.55.46.png)
		3. `ShopSettingsOperationalHoursActivity`
		
		
			1. Description: You can use this feature to configure your shop operational hours
			2. Applink: `tokopedia-android-internal://marketplace/shop-settings-operational-hours` 
			
			![](res/Screen%20Shot%202023-05-17%20at%2014.57.31.png)
		4. ShopSettingsSetOperationalHoursActivity
		
		
			1. Description: You can use this feature to configure your shop operational hours 
			
			![](res/Screen%20Shot%202023-05-17%20at%2015.52.53.png)
3. common: Contains some common UI (CustomView, menu bottom sheet & utils)
4. etalase: 


	1. Description: You can use this feature to add new showcase/ etalase to your shop page
	2. Applink:`tokopedia-android-internal://marketplace/shop-settings-etalase/add`
	3. Screenshot: 
	
	![](res/Screen%20Shot%202023-05-09%20at%2010.30.06-20230509-033010.png)
5. notes: 


	1. Description: You can use this feature to add notes to your shop
	2. Gql: 
	
	
		1. `addShopNote` [Shop Note Mutation](/wiki/spaces/MC/pages/1253411537/Shop+Note+Mutation)
		2. `deleteShopNote` [Shop Note Mutation](/wiki/spaces/MC/pages/1253411537/Shop+Note+Mutation)
		3. `shopNotes`[Shop Note Mutation](/wiki/spaces/MC/pages/1253411537/Shop+Note+Mutation)
		4. `reorderShopNote` [Shop Note Mutation](/wiki/spaces/MC/pages/1253411537/Shop+Note+Mutation)
		5. `updateShopNote` [Shop Note Mutation](/wiki/spaces/MC/pages/1253411537/Shop+Note+Mutation)
	3. Applink:`tokopedia-android-internal://marketplace/shop-settings-notes` 
	
	![](res/Screen%20Shot%202023-05-17%20at%2014.20.42.png)
6. setting: 


	1. Description: You can use this feature to configure your shop
	2. Applink: 
	
	
		1. `tokopedia-android-internal://marketplace/shop-page-setting`
		2. `tokopedia-android-internal://marketplace/shop-page/{shop_id}/settings`
		
		![](res/Screen%20Shot%202023-05-17%20at%2014.26.11.png)



| **Param Name** | **Mandatory** | **Description** | **Example** |
| --- | --- | --- | --- |
| `shop_id` | Yes | ID that specify which shop to get the detail. This paramater will be passed to the back-end. | `123456` |

### Background

We use this feature/ module to configure our shop settings (Operational hours, Shop Open & Close, Shop Notes, Add New Etalase/ Showcase & Change Shop Name/ Shop Domain)

## Tech Stack

- Kotlin
- LiveData
- Coroutine
- Gql

## Useful Links

- [Figma Operational Hours](https://www.figma.com/file/7bW4yl5nD4pln0urZHArOI/%5BD%2FM%5D-Shop-Operational-Hour-V.2?node-id=2863%3A107217)
- [Figma Change Shop Name/ Shop Domain](https://www.figma.com/file/Bsc6WyIVxy84b4MjrpqU8Y/%5BD%5D---UI-Shop-Changing-Name?type=design&node-id=1-91)
