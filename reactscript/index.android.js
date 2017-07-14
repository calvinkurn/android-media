'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
import { NetworkModule } from 'NativeModules';
import { NavigationModule } from 'NativeModules';

import { ContactUs_Stack, HotList_Stack, Favorite_Stack, OfficialStore } from './src/configs/router';


class Home extends Component { 
  componentWillMount() {
    console.log(this.props.Screen)
  }

  render(){ 
    if (this.props.message == 'contactus'){
      return <ContactUs_Stack />  
    } else if (this.props.message == 'hotlist'){
      return <HotList_Stack /> 
    } else if (this.props.message == 'favorite'){
      return <Favorite_Stack /> 
    } else if (this.props.Screen == 'official-store') {
      return <OfficialStore />ot
    } else {
      return(
        <View style={{justifyContent:'center', alignItems:'center', flex:1}}>
          <Text>No Props!</Text>
        </View>
      )
    }
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});


module.exports = Home;
AppRegistry.registerComponent('MAIN', () => Home);

