'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  ActivityIndicator,
  StyleSheet,
  Text,
  AppState,
  UIManager,
  View
} from 'react-native'
import OfficialStore from './src/pages/official-store/setup'
import ThankYou from './src/pages/thankyou-page/setup'



UIManager.setLayoutAnimationEnabledExperimental && UIManager.setLayoutAnimationEnabledExperimental(true);

class Home extends Component {
  render(){
    if (this.props.Screen === 'official-store'){
      return <OfficialStore />
    } else if (this.props.Screen === 'thankyou-page') {
        return <ThankYou data={this.props}/>
    } else {
      return (
        <View style={{ marginTop:20, marginBottom:20, justifyContent:'center', alignItems:'center', flex:1}}>
          <ActivityIndicator size="large" />
        </View>
      )
    }
  }
}
const styles = StyleSheet.create({
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

