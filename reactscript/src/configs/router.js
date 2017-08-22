import React from 'react'
import { TabNavigator, StackNavigator } from 'react-navigation'
import Official_Store_Page from '../pages/official-store/setup';
import HotList from '../pages/hotlist/index.hotlist';

const violet = '#7950f2';
const blue = '#228ae6';
const green = '#37b24d';
const orange = '#fd7e14';
const white = '#FFFFFF';



// export const OfficialStore = StackNavigator({
//     OfficalStore: {
//         screen: Official_Store_Page,
//         navigationOptions: {
//             title: 'Official Store',
//             headerTintColor: white,
//             headerStyle: {
//                 backgroundColor: '#42b549'
//             }
//         }
//     }
// })

export const HotList_ = StackNavigator({
    OfficalStore: {
        screen: HotList,
        navigationOptions: {
            title: 'HotList',
            headerTintColor: white,
            headerStyle: {
                backgroundColor: '#42b549'
            }
        }
    }
})