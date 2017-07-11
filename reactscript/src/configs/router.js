import React from 'react'
import { TabNavigator, StackNavigator } from 'react-navigation'

import ContactUs_Page from '../pages/contactus/Contactus'; 
import Page_Dua from '../pages/contactus/SecondPage'; 

import HotList_Page from '../pages/hotlist/Hotlist'; 
import HotList_Page_Dua from '../pages/hotlist/SecondPage'; 

import Favorite_Page from '../pages/favorite/Favorite'; 
import Favorite_Page_Dua from '../pages/favorite/SecondPage'; 

import Official_Store_Page from '../pages/official-store/setup';


const violet = '#7950f2';
const blue = '#228ae6';
const green = '#37b24d';
const orange = '#fd7e14'; 
const white = '#FFFFFF';

export const ContactUs_Stack = StackNavigator({
    ContactUs: {
        screen: ContactUs_Page,
        path: 'contactus/',
        navigationOptions: {
            title: 'Contact Us',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: green
            }
        }
    },
    PageDua: {
        screen: Page_Dua, 
        navigationOptions: {
            title: 'Page 2',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: violet
            }
        }
    } 
});


export const HotList_Stack = StackNavigator({
    HotList: {
        screen: HotList_Page, 
        navigationOptions: {
            title: 'Hot List',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: green
            }
        }
    },
    PageDua: {
        screen: HotList_Page_Dua, 
        navigationOptions: {
            title: 'Page 2',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: violet
            }
        }
    } 
});


export const Favorite_Stack = StackNavigator({
    Favorite: {
        screen: Favorite_Page, 
        navigationOptions: {
            title: 'Favorite',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: green
            }
        }
    },
    PageDua: {
        screen: Favorite_Page_Dua, 
        navigationOptions: {
            title: 'Page 2',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: violet
            }
        }
    } 
});


export const OfficialStore = StackNavigator({
    OfficalStore: {
        screen: Official_Store_Page,
        navigationOptions: {
            title: 'Official Store',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: '#42b549'
            }
        }
    }
})