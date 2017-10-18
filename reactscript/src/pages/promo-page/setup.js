import React, { Component } from 'react'
import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native'
import { Provider } from 'react-redux'
import store from './store/Store'
import App from './components/App'
import TermsConditions from './components/TermsConditions'
import { StackNavigator } from 'react-navigation'


const PromoApp = StackNavigator({
    App : { screen: App },
    TermsPage: { screen: TermsConditions }
})

class Root extends Component {
    componentDidMount(){
        console.log(this.props.data)
    }

    render() {
        return (
            <Provider store={store}>
                <PromoApp screenProps={this.props.data} />
            </Provider>
        )
    }
}

export default Root